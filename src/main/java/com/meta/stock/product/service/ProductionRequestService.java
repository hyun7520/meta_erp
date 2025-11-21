package com.meta.stock.product.service;

import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.lots.dto.LotStockDto;
import com.meta.stock.lots.mapper.LotsMapper;
import com.meta.stock.product.dto.ProductRequestDto;
import com.meta.stock.product.dto.ProductionRequestDTO;
import com.meta.stock.product.entity.ProductionRequestEntity;
import com.meta.stock.product.entity.ProductEntity;
import com.meta.stock.product.mapper.ProductMapper;
import com.meta.stock.product.mapper.ProductionRequestMapper;
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

    public Page<ProductRequestDto> findOngoingProductRequests(String keyword, Pageable pageable) {
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

    public void acceptProductionRequest(long prId) {
        ProductionRequestEntity productionRequest = productionRequestRepository.findProductRequestByPrId(prId);
        productionRequest.setProductionStartDate(String.valueOf(LocalDate.now()));
        productionRequestRepository.save(productionRequest);
    }

    public ProductRequestDto findProductRequestById(long orderId) {
        return prMapper.findProductRequestById(orderId);
    }

    @Transactional
    public void shipOrder(Long prId) {
        ProductRequestDto pr = findProductRequestById(prId);

        if (pr.getProductionStartDate() == null) {
            throw new IllegalStateException("수주된 주문만 출하 가능합니다.");
        }

        List<LotStockDto> lots = lotsMapper.findLotsBySerialCodeOrderByExpiry(pr.getSerialCode());

        if (lots.isEmpty()) {
            throw new IllegalStateException("출하할 재고가 없습니다.");
        }

        int totalStock = lots.stream().mapToInt(LotStockDto::getQty).sum();
        if (totalStock < pr.getTargetQty()) {
            throw new IllegalStateException("재고가 부족합니다. (필요: " + pr.getTargetQty() + ", 현재: " + totalStock + ")");
        }

        int remainingQty = pr.getTargetQty();

        for (LotStockDto lot : lots) {
            if (remainingQty <= 0) break;

            int currentQty = lot.getQty();
            if (currentQty <= 0) continue;

            int toShip = Math.min(currentQty, remainingQty);
            int newQty = currentQty - toShip;
            remainingQty -= toShip;

            lotsMapper.updateLotQty(lot.getLotId(), newQty);

            if (newQty == 0) {
                lotsMapper.deleteLotById(lot.getLotId());
                productMapper.deleteProductByLotId(lot.getLotId());
            }
        }

        if (remainingQty == 0) {
            prMapper.updateEndDate(prId);
        } else {
            throw new IllegalStateException("출하 처리 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public void acceptOrder(Long prId) {
        prMapper.updateProductionStartDate(prId);
    }

    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getAllOrders() {
        List<ProductionRequestEntity> entities = productionRequestRepository.findAllByOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getPendingOrders() {
        List<ProductionRequestEntity> entities = productionRequestRepository.findByProductionStartDateIsNullOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getInProgressOrders() {
        List<ProductionRequestEntity> entities = productionRequestRepository.findByProductionStartDateIsNotNullAndEndDateIsNullOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductionRequestDTO.Response> getCompletedOrders() {
        List<ProductionRequestEntity> entities = productionRequestRepository.findByEndDateIsNotNullOrderByPrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    //  생산 요청 생성 (Repository 쿼리 메서드 사용)
    @Transactional
    public ProductionRequestDTO.Response createProductionRequest(ProductionRequestDTO.Request request) {
        String serialCode = null;

        if (request.getProductId() != null) {
            serialCode = productRepository.findProductNameById(request.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다. ID: " + request.getProductId()));
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

    @Transactional
    public ProductionRequestDTO.Response startProduction(long prId) {
        ProductionRequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        String now = LocalDateTime.now().format(DATE_FORMATTER);
        entity.setProductionStartDate(now);

        ProductionRequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }

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

    @Transactional
    public ProductionRequestDTO.Response updateProductionStatus(ProductionRequestDTO.UpdateStatus update) {
        ProductionRequestEntity entity = productionRequestRepository.findById(update.getPrId())
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        String now = LocalDateTime.now().format(DATE_FORMATTER);

        if (update.getAction() == 1) {
            entity.setProductionStartDate(now);
            entity.setEndDate(null);
        } else if (update.getAction() == 2) {
            if (entity.getProductionStartDate() == null) {
                entity.setProductionStartDate(now);
            }
            entity.setEndDate(now);
            entity.setCompletedQty(entity.getTargetQty());
        } else if (update.getAction() == 3) {
            entity.setProductionStartDate(null);
            entity.setEndDate(null);
            entity.setCompletedQty(0);
        } else {
            throw new IllegalArgumentException("잘못된 상태 변경 요청입니다.");
        }

        ProductionRequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    @Transactional
    public void deleteProductionRequest(long prId) {
        ProductionRequestEntity entity = productionRequestRepository.findById(prId)
                .orElseThrow(() -> new IllegalArgumentException("생산 요청을 찾을 수 없습니다."));

        productionRequestRepository.delete(entity);
    }

    @Transactional
    public void deleteCompletedOrders() {
        List<ProductionRequestEntity> completedOrders = productionRequestRepository.findByEndDateIsNotNullOrderByPrIdDesc();
        productionRequestRepository.deleteAll(completedOrders);
    }

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

        if (request.getRequestBy() != null && !request.getRequestBy().isEmpty()) {
            entity.setToCompany(request.getRequestBy());
        }

        if (request.getQty() > 0) {
            entity.setTargetQty(request.getQty());
        }

        if (request.getUnit() != null && !request.getUnit().isEmpty()) {
            entity.setUnit(request.getUnit());
        }

        if (request.getDeadline() != null && !request.getDeadline().isEmpty() && !request.getDeadline().equals("-")) {
            entity.setDeadline(request.getDeadline());
        }

        ProductionRequestEntity saved = productionRequestRepository.save(entity);
        return convertToResponse(saved);
    }
}