package com.meta.stock.product.service;

import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.lots.dto.LotStockDto;
import com.meta.stock.lots.mapper.LotsMapper;
import com.meta.stock.product.dto.ProductDto;
import com.meta.stock.product.dto.ProductRequestDto;
import com.meta.stock.product.entity.ProductionRequestEntity;
import com.meta.stock.product.mapper.ProductMapper;
import com.meta.stock.product.mapper.ProductionRequestMapper;
import com.meta.stock.product.repository.ProductionRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    // id로 production request 생산 시작
    public void acceptProductionRequest(long prId) {
        ProductionRequestEntity productionRequest = productionRequestRepository.findProductRequestByPrId(prId);
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

    // 주문 수주
    @Transactional
    public void acceptOrder(Long prId) {
        prMapper.updateProductionStartDate(prId);
    }
}
