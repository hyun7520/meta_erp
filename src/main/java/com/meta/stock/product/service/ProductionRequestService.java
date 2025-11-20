package com.meta.stock.product.service;

import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.order.repository.LotsRepository;
import com.meta.stock.order.dto.LotStockDto;
import com.meta.stock.order.mapper.LotsMapper;
import com.meta.stock.product.dto.ProductDto;
import com.meta.stock.product.dto.ProductRequestDto;
import com.meta.stock.product.entity.ProductionRequestEntity;
import com.meta.stock.product.mapper.ProductMapper;
import com.meta.stock.product.mapper.ProductionRequestMapper;
import com.meta.stock.product.repository.ProductionRequestRepository;
import com.meta.stock.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ProductionRequestMapper productionRequestMapper;
    @Autowired
    private ProductionRequestRepository productionRequestRepository;
    @Autowired
    private MaterialService materialService;

    // keyword 0, 1, 2에 따라 production request 가져오기
    public List<ProductRequestDto> findOngoingProductRequests() {
        return prMapper.findOngoingProductRequests();
    }

    public List<ProductRequestDto> findAllProductionRequests() {
        return prMapper.findAllProductionRequests();
    }

    // id로 production request 생산 시작
    public void acceptProductionRequest(long orderId) {
        ProductionRequestEntity productionRequest = productionRequestRepository.findProductRequestById(orderId);
        productionRequest.setProductionStartDate(String.valueOf(LocalDate.now()));
        productionRequestRepository.save(productionRequest);
    }

    // id로 production request 조회
    public ProductRequestDto findProductRequestById(long orderId) {
        return prMapper.findProductRequestById(orderId);
    }

    // 주문에 대한 제품 출하
    public void shipOrder(Long orderId) {
        ProductRequestDto productRequestDto = prMapper.findProductRequestById(orderId);
        List<ProductDto> productDto = productMapper.getProductsByPRId(productRequestDto.getPrId());
        if (productRequestDto.getProductionStartDate() == null) {
            throw new IllegalStateException("수주된 주문만 출하 가능합니다.");
        }
        long productId = productDto.get(0).getProductId();

        // 2. 유통기한 빠른 순으로 로트 조회
        List<LotStockDto> lots = lotsMapper.findLotsByProductIdOrderByExpiry(productId);
        if (lots.isEmpty()) {
            throw new IllegalStateException("출하할 재고가 없습니다.");
        }

        int remainingQty = productRequestDto.getCompletedQty();
        for (LotStockDto lot : lots) {
            if (remainingQty <= 0) break;

            int currentQty = lot.getQty();
            if (currentQty <= 0) continue;

            int toShip = Math.min(currentQty, remainingQty);  // 이번 로트에서 뺄 수량
            int newQty = currentQty - toShip;
            remainingQty -= toShip;

            // 3. 로트 재고 업데이트
            lotsMapper.updateLotQty(lot.getLotId(), newQty);

            // 재고 0이면 로트 삭제
            if (newQty == 0) {
                lotsMapper.deleteZeroLot(lot.getLotId());
            }
        }

        if (remainingQty > 0) {
            throw new IllegalStateException("재고 부족: " + remainingQty + "개 출하 불가능");
        }

        // 4. 주문 상태를 출하 완료로 변경 (COMPLETE = 2)
        productionRequestMapper.updateOrderStatus(orderId, 2);
    }

    public List<MaterialRequirementDto> calculateStock(List<MaterialRequirementDto> requirements) {
        requirements.forEach(req -> {
            int stock = materialService.getCurrentStock(req.getMaterialName());
            req.setCurrentStock(stock);
            req.setSufficient(stock >= req.getRequiredQty());
        });
        return requirements;
    }

    public List<MaterialRequirementDto> calculateRemaining(List<MaterialRequirementDto> remaining) {
        remaining.forEach(req -> {
            int stock = materialService.getCurrentStock(req.getMaterialName());
            req.setCurrentStock(stock);
            req.setRemainingQty(Math.max(0, req.getRequiredQty() - stock));
        });
        return remaining;
    }
}
