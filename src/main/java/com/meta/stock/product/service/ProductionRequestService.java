package com.meta.stock.product.service;

import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.lots.dto.LotStockDto;
import com.meta.stock.lots.mapper.LotsMapper;
import com.meta.stock.product.dto.ProductRequestDto;
import com.meta.stock.product.dto.ProductionRequestDTO;
import com.meta.stock.product.entity.FixedProductEntity;
import com.meta.stock.product.entity.ProductionRequestEntity;
import com.meta.stock.product.entity.ProductEntity;
import com.meta.stock.product.mapper.ProductMapper;
import com.meta.stock.product.mapper.ProductionRequestMapper;
import com.meta.stock.product.repository.FixedProductRepository;
import com.meta.stock.product.repository.ProductionRequestRepository;
import com.meta.stock.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductionRequestService {

    @Autowired
    private ProductionRequestMapper prMapper;
    @Autowired
    private LotsMapper lotsMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductionRequestRepository productionRequestRepository;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FixedProductRepository fixedProductRepository;
    @Autowired
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Page<ProductRequestDto> findAllProductRequests(String keyword, Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().next().getProperty();
        String sortDir = pageable.getSort().iterator().next().getDirection().name();

        List<ProductRequestDto> content = prMapper.findAllRequestsWithPaging(
                keyword, sortBy, sortDir, offset, limit
        );

        long total = prMapper.countAllRequests(keyword);

        return new PageImpl<>(content, pageable, total);
    }

    // keyword 0, 1, 2에 따라 production request 가져오기
    public Page<ProductRequestDto> findOngoingRequestsWithPaging(String keyword, Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().next().getProperty();
        String sortDir = pageable.getSort().iterator().next().getDirection().name();

        List<ProductRequestDto> content = prMapper.findOngoingRequestsWithPaging(
                keyword, sortBy, sortDir, offset, limit
        );

        long total = prMapper.countOngoingRequests(keyword);

        return new PageImpl<>(content, pageable, total);
    }

    // id로 production request 생산 시작
    public void acceptProductionRequest(long prId) {
        ProductionRequestEntity productionRequest = productionRequestRepository.findProductRequestByPrId(prId);
        productionRequest.setProductionEmployee(1L);
        productionRequest.setPlannedQty((int) (productionRequest.getTargetQty() + productionRequest.getTargetQty() * Math.random()));
        productionRequest.setProductionStartDate(String.valueOf(LocalDate.now()));
        productionRequestRepository.save(productionRequest);
    }

    // id로 production request 조회
    public ProductRequestDto findProductRequestById(long orderId) {
        return prMapper.findProductRequestById(orderId);
    }

    // 제품 출하
    @Transactional
    public void shipOrder(Long prId) {
        // 1. 생산 요청 정보 조회
        ProductRequestDto pr = findProductRequestById(prId);

        // 수주 확인
        if (pr.getProductionStartDate() == null) {
            throw new IllegalStateException("수주된 주문만 출하 가능합니다.");
        }

        // 2. 해당 제품의 로트를 유통기한 빠른 순으로 조회
        List<LotStockDto> lots = lotsMapper.findLotsBySerialCodeOrderByExpiry(pr.getSerialCode());

        if (lots.isEmpty()) {
            throw new IllegalStateException("출하할 재고가 없습니다.");
        }

        // 3. 총 재고 확인
        int totalStock = lots.stream().mapToInt(LotStockDto::getQty).sum();
        if (totalStock < pr.getTargetQty()) {
            throw new IllegalStateException("재고가 부족합니다. (필요: " + pr.getTargetQty() + ", 현재: " + totalStock + ")");
        }

        // 4. 유통기한 빠른 순으로 재고 차감
        int remainingQty = pr.getTargetQty();

        for (LotStockDto lot : lots) {
            if (remainingQty <= 0) break;

            int currentQty = lot.getQty();
            if (currentQty <= 0) continue;

            // 이번 로트에서 출하할 수량
            int toShip = Math.min(currentQty, remainingQty);
            int newQty = currentQty - toShip;
            remainingQty -= toShip;

            // 5. 로트 재고 업데이트
            lotsMapper.updateLotQty(lot.getLotId(), newQty);

            // 재고가 0이 되면 로트 삭제 (선택사항)
            if (newQty == 0) {
                lotsMapper.deleteLotById(lot.getLotId());
                // 또는 Products 테이블에서 해당 로트 연결 제거
                productMapper.deleteProductByLotId(lot.getLotId());
            }
        }

        // 6. 주문 완료 처리
        if (remainingQty == 0) {
            prMapper.updateEndDate(prId);
        } else {
            throw new IllegalStateException("출하 처리 중 오류가 발생했습니다.");
        }
    }

    public Map<String, Integer> getStatusStatistics() {
        Map<String, Integer> stats = new HashMap<>();

        // 완료 (end_date가 있음)
        int completed = prMapper.countCompleted();

        // 기간 초과 (deadline < 오늘 && end_date = null)
        int overdue = prMapper.countOverdue();

        // 진행 중 (production_start_date 있음 && end_date = null && deadline >= 오늘)
        int ongoing = prMapper.countOngoing();

        // 미수주 (production_start_date = null)
        int pending = prMapper.countPending();

        stats.put("completed", completed);
        stats.put("overdue", overdue);
        stats.put("ongoing", ongoing);
        stats.put("pending", pending);

        return stats;
    }

    // 전체 주문 목록 조회
    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getAllOrders() {
        List<ProductionRequestEntity> entities = productionRequestRepository.findAllByOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 대기중 주문 목록 조회
    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getPendingOrders() {
        List<ProductionRequestEntity> entities = productionRequestRepository.findByProductionStartDateIsNullOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 생산시작 주문 목록 조회
    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getInProgressOrders() {
        List<ProductionRequestEntity> entities = productionRequestRepository.findByProductionStartDateIsNotNullAndEndDateIsNullOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 생산완료 주문 목록 조회
    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getCompletedOrders() {
        List<ProductionRequestEntity> entities = productionRequestRepository.findByEndDateIsNotNullOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 생산 요청 생성
    @Transactional
    public ProductionRequestDTO.Response createProductionRequest(ProductionRequestDTO.Request request) {
        String serialCode = null;
        if (request.getProductId() != null) {
            FixedProductEntity fixedProduct = fixedProductRepository.findById(request.getProductId().intValue())
                    .orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다. ID: " + request.getProductId()));
            serialCode = fixedProduct.getName();
        } else if (request.getSerialCode() != null) {
            serialCode = request.getSerialCode();
        } else {
            throw new IllegalArgumentException("productId 또는 serialCode가 필요합니다.");
        }

        ProductionRequestEntity entity = ProductionRequestEntity.builder()
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

        ProductionRequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    // 생산 시작 처리
    @Transactional
    public ProductionRequestDTO.Response startProduction(long prId) {
        ProductionRequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        String now = LocalDateTime.now().format(DATE_FORMATTER);
        entity.setProductionStartDate(now);

        ProductionRequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    // 생산 완료 처리
    @Transactional
    public ProductionRequestDTO.Response completeProduction(long prId) {
        ProductionRequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        String now = LocalDateTime.now().format(DATE_FORMATTER);
        if (entity.getProductionStartDate() == null) {
            entity.setProductionStartDate(now);
        }
        entity.setEndDate(now);
        entity.setCompletedQty(entity.getTargetQty());

        ProductionRequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    // 생산 상태 업데이트 (기존 메서드 유지)
    @Transactional
    public ProductionRequestDTO.Response updateProductionStatus(ProductionRequestDTO.UpdateStatus update) {
        ProductionRequestEntity entity = productionRequestRepository.findById(update.getPrId())
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

        ProductionRequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    // 생산 요청 삭제
    @Transactional
    public void deleteProductionRequest(long prId) {
        ProductionRequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        productionRequestRepository.delete(entity);
    }

    // 생산완료 주문 일괄 삭제
    @Transactional
    public void deleteCompletedOrders() {
        List<ProductionRequestEntity> completedOrders = productionRequestRepository.findByEndDateIsNotNullOrderByPrIdDesc();
        productionRequestRepository.deleteAll(completedOrders);
    }

    // 인기 상품 조회 (단일)
    @Transactional(readOnly = true)
    public ProductionRequestDTO.TopProduct getTopProduct() {
        List<ProductionRequestEntity> allOrders = productionRequestRepository.findAll();

        if (allOrders.isEmpty()) {
            return null;
        }

        Map<String, ProductionRequestDTO.TopProduct> productMap = allOrders.stream()
                .filter(order -> order.getSerialCode() != null)
                .collect(Collectors.groupingBy(
                        ProductionRequestEntity::getSerialCode,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                orders -> {
                                    int totalQty = orders.stream().mapToInt(ProductionRequestEntity::getTargetQty).sum();
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
        List<ProductionRequestEntity> allOrders = productionRequestRepository.findAll();

        Map<String, ProductionRequestDTO.TopProduct> productMap = allOrders.stream()
                .filter(order -> order.getSerialCode() != null)
                .collect(Collectors.groupingBy(
                        ProductionRequestEntity::getSerialCode,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                orders -> {
                                    int totalQty = orders.stream().mapToInt(ProductionRequestEntity::getTargetQty).sum();
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
    private ProductionRequestDTO.Response convertToResponse(ProductionRequestEntity entity) {
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

    @Transactional(readOnly = true)
    public ProductionRequestDTO.Response getProductionRequestById(long prId) {
        ProductionRequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));
        return convertToResponse(entity);
    }

    // 생산 요청 수정 (Repository 쿼리 메서드 사용)
    @Transactional
    public ProductionRequestDTO.Response updateProductionRequest(long prId, ProductionRequestDTO.Request request) {
        ProductionRequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        // 생산 시작 전에만 수정 가능하도록 제한
        if (entity.getProductionStartDate() != null) {
            throw new IllegalStateException("이미 생산이 시작된 주문은 수정할 수 없습니다.");
        }

        // Serial Code 업데이트
        if (request.getProductId() != null) {
            String serialCode = productRepository.findProductNameById(request.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다. ID: " + request.getProductId()));
            entity.setSerialCode(serialCode);
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

        ProductionRequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    public int getOngoingRequestsCount(String prKeyword) {
        return prMapper.getOngoingRequestsCount(prKeyword);
    }
}
