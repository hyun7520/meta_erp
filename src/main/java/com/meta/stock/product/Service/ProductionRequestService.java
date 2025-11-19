package com.meta.stock.product.Service;

import com.meta.stock.product.DTO.ProductionRequestDTO;
import com.meta.stock.product.Entity.Production_RequestEntity;
import com.meta.stock.product.Entity.ProductsEntity;
import com.meta.stock.product.Repository.ProductionRequestRepository;
import com.meta.stock.product.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductionRequestService {

    private final ProductionRequestRepository productionRequestRepository;
    private final ProductRepository productRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ProductionRequestService(ProductionRequestRepository productionRequestRepository,
                                    ProductRepository productRepository) {
        this.productionRequestRepository = productionRequestRepository;
        this.productRepository = productRepository;
    }

    // 전체 주문 목록 조회
    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getAllOrders() {
        List<Production_RequestEntity> entities = productionRequestRepository.findAllByOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 대기중 주문 목록 조회
    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getPendingOrders() {
        List<Production_RequestEntity> entities = productionRequestRepository.findByProductionStartDateIsNullOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 생산시작 주문 목록 조회
    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getInProgressOrders() {
        List<Production_RequestEntity> entities = productionRequestRepository.findByProductionStartDateIsNotNullAndEndDateIsNullOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 생산완료 주문 목록 조회
    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getCompletedOrders() {
        List<Production_RequestEntity> entities = productionRequestRepository.findByEndDateIsNotNullOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 생산 요청 생성
    @Transactional
    public ProductionRequestDTO.Response createProductionRequest(ProductionRequestDTO.Request request) {
        String serialCode = null;
        if (request.getProductId() != null) {
            ProductsEntity product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다. ID: " + request.getProductId()));
            serialCode = product.getProductName();
        } else if (request.getSerialCode() != null) {
            serialCode = request.getSerialCode();
        } else {
            throw new IllegalArgumentException("productId 또는 serialCode가 필요합니다.");
        }

        Production_RequestEntity entity = Production_RequestEntity.builder()
                .serialCode(serialCode)
                .managementEmployee(0)
                .productionEmployee(0)
                .toCompany(request.getRequestBy())
                .targetQty(request.getQty())
                .plannedQty(0)
                .completedQty(0)
                .unit(request.getUnit())
                .requestDate(LocalDate.now().format(DATE_FORMATTER))
                .deadline(request.getDeadline())
                .note("")
                .build();

        Production_RequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    // 생산 시작 처리
    @Transactional
    public ProductionRequestDTO.Response startProduction(long prId) {
        Production_RequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        String now = LocalDateTime.now().format(DATE_FORMATTER);
        entity.setProductionStartDate(now);

        Production_RequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    // 생산 완료 처리
    @Transactional
    public ProductionRequestDTO.Response completeProduction(long prId) {
        Production_RequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        String now = LocalDateTime.now().format(DATE_FORMATTER);
        if (entity.getProductionStartDate() == null) {
            entity.setProductionStartDate(now);
        }
        entity.setEndDate(now);
        entity.setCompletedQty(entity.getTargetQty());

        Production_RequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    // 생산 상태 업데이트 (기존 메서드 유지)
    @Transactional
    public ProductionRequestDTO.Response updateProductionStatus(ProductionRequestDTO.UpdateStatus update) {
        Production_RequestEntity entity = productionRequestRepository.findById(update.getPrId())
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        String now = LocalDateTime.now().format(DATE_FORMATTER);

        if (update.getAction() == 1) { // 생산 시작
            entity.setProductionStartDate(now);
            entity.setEndDate(null);
        } else if (update.getAction() == 2) { // 생산 완료
            if (entity.getProductionStartDate() == null) {
                entity.setProductionStartDate(now);
            }
            entity.setEndDate(now);
            entity.setCompletedQty(entity.getTargetQty());
        } else if (update.getAction() == 3) { // 생산 취소/대기
            entity.setProductionStartDate(null);
            entity.setEndDate(null);
            entity.setCompletedQty(0);
        } else {
            throw new IllegalArgumentException("잘못된 상태 변경 요청입니다.");
        }

        Production_RequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    // 생산 요청 삭제
    @Transactional
    public void deleteProductionRequest(long prId) {
        Production_RequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        productionRequestRepository.delete(entity);
    }

    // 생산완료 주문 일괄 삭제
    @Transactional
    public void deleteCompletedOrders() {
        List<Production_RequestEntity> completedOrders = productionRequestRepository.findByEndDateIsNotNullOrderByPrIdDesc();
        productionRequestRepository.deleteAll(completedOrders);
    }

    // 인기 상품 조회 (단일)
    @Transactional(readOnly = true)
    public ProductionRequestDTO.TopProduct getTopProduct() {
        List<Production_RequestEntity> allOrders = productionRequestRepository.findAll();

        if (allOrders.isEmpty()) {
            return null;
        }

        Map<String, ProductionRequestDTO.TopProduct> productMap = allOrders.stream()
                .filter(order -> order.getSerialCode() != null)
                .collect(Collectors.groupingBy(
                        Production_RequestEntity::getSerialCode,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                orders -> {
                                    int totalQty = orders.stream().mapToInt(Production_RequestEntity::getTargetQty).sum();
                                    String unit = orders.get(0).getUnit();
                                    return ProductionRequestDTO.TopProduct.builder()
                                            .productName(orders.get(0).getSerialCode())
                                            .totalQty(totalQty)
                                            .unit(unit != null ? unit : "개")
                                            .orderCount(orders.size())
                                            .build();
                                }
                        )
                ));

        return productMap.values().stream()
                .max((a, b) -> Integer.compare(a.getTotalQty(), b.getTotalQty()))
                .orElse(null);
    }

    // 인기 상품 목록 조회 (여러 개)
    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.TopProduct> getTopSellingProducts() {
        List<Production_RequestEntity> allOrders = productionRequestRepository.findAll();

        Map<String, ProductionRequestDTO.TopProduct> productMap = allOrders.stream()
                .filter(order -> order.getSerialCode() != null)
                .collect(Collectors.groupingBy(
                        Production_RequestEntity::getSerialCode,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                orders -> {
                                    int totalQty = orders.stream().mapToInt(Production_RequestEntity::getTargetQty).sum();
                                    String unit = orders.get(0).getUnit();
                                    return ProductionRequestDTO.TopProduct.builder()
                                            .productName(orders.get(0).getSerialCode())
                                            .totalQty(totalQty)
                                            .unit(unit != null ? unit : "개")
                                            .orderCount(orders.size())
                                            .build();
                                }
                        )
                ));

        return productMap.values().stream()
                .sorted((a, b) -> Integer.compare(b.getTotalQty(), a.getTotalQty()))
                .collect(Collectors.toList());
    }

    // Entity -> Response DTO 변환
    private ProductionRequestDTO.Response convertToResponse(Production_RequestEntity entity) {
        String productName = entity.getSerialCode() != null ? entity.getSerialCode() : "-";

        return ProductionRequestDTO.Response.builder()
                .orderId(entity.getPrId())
                .serialCode(entity.getSerialCode())
                .productName(productName)
                .requestBy(entity.getToCompany())
                .qty(entity.getTargetQty())
                .unit(entity.getUnit())
                .requestDate(entity.getRequestDate())
                .deadline(entity.getDeadline())
                .complete(entity.getCompleteStatus())
                .productionStartDate(entity.getProductionStartDate())
                .endDate(entity.getEndDate())
                .build();
    }
    // 단일 생산 요청 조회
    @Transactional(readOnly = true)
    public ProductionRequestDTO.Response getProductionRequestById(long prId) {
        Production_RequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));
        return convertToResponse(entity);
    }

    // 생산 요청 수정
    @Transactional
    public ProductionRequestDTO.Response updateProductionRequest(long prId, ProductionRequestDTO.Request request) {
        Production_RequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        // 생산 시작 전에만 수정 가능하도록 제한
        if (entity.getProductionStartDate() != null) {
            throw new IllegalStateException("이미 생산이 시작된 주문은 수정할 수 없습니다.");
        }

        // Serial Code 업데이트
        if (request.getProductId() != null) {
            ProductsEntity product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다. ID: " + request.getProductId()));
            entity.setSerialCode(product.getProductName());
        } else if (request.getSerialCode() != null && !request.getSerialCode().isEmpty()) {
            entity.setSerialCode(request.getSerialCode());
        }

        // 필드 업데이트 - null 체크 추가
        if (request.getRequestBy() != null && !request.getRequestBy().isEmpty()) {
            entity.setToCompany(request.getRequestBy());
        }

        if (request.getQty() > 0) {
            entity.setTargetQty(request.getQty());
        }

        if (request.getUnit() != null && !request.getUnit().isEmpty()) {
            entity.setUnit(request.getUnit());
        }

        // 날짜 필드 - null이나 빈 문자열 체크
        if (request.getDeadline() != null && !request.getDeadline().isEmpty() && !request.getDeadline().equals("-")) {
            entity.setDeadline(request.getDeadline());
        }

        Production_RequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }
}