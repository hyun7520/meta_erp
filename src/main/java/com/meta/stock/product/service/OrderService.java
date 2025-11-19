package com.meta.stock.product.service;

import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.order.repository.LotsRepository;
import com.meta.stock.order.dto.LotStockDto;
import com.meta.stock.product.entity.OrderEntity;
import com.meta.stock.order.mapper.LotsMapper;
import com.meta.stock.product.dto.OrderDto;
import com.meta.stock.product.mapper.OrderMapper;
import com.meta.stock.product.mapper.ProductMapper;
import com.meta.stock.product.repository.OrderRepository;
import com.meta.stock.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private LotsMapper lotsMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private LotsRepository lotsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MaterialService materialService;

    public List<OrderDto> findAllOrders(int keyword) {
        return orderMapper.findAllOrders(keyword);
    }

    public OrderDto findOrderById(long orderId) {
        return orderMapper.findOrderById(orderId);
    }

    public void acceptOrder(long orderId) {
        OrderEntity order = orderRepository.findByOrderId(orderId);
        order.setComplete(1);
        orderRepository.save(order);
    }

    public void shipOrder(Long orderId) {
        OrderDto order = orderMapper.findOrderById(orderId);
        if (order.getComplete() != 1) {
            throw new IllegalStateException("수주된 주문만 출하 가능합니다.");
        }

        int requiredQty = order.getQty();
        Long productId = order.getProductDto().getProductId();

        // 2. 유통기한 빠른 순으로 로트 조회
        List<LotStockDto> lots = lotsMapper.findLotsByProductIdOrderByExpiry(productId);
        for(LotStockDto lot : lots) {
            System.out.println(lot.getLotId());
            System.out.println(lot.getQty());
        }

        if (lots.isEmpty()) {
            throw new IllegalStateException("출하할 재고가 없습니다.");
        }

        int remainingQty = requiredQty;
        for (LotStockDto lot : lots) {
            if (remainingQty <= 0) break;

            int currentQty = lot.getQty();
            if (currentQty <= 0) continue;

            int toShip = Math.min(currentQty, remainingQty);  // 이번 로트에서 뺄 수량
            int newQty = currentQty - toShip;
            remainingQty -= toShip;

            // 3. 로트 재고 업데이트
            lotsMapper.updateLotQty(lot.getLotId(), newQty);

            // 재고 0이면 로트 삭제 (선택사항)
            if (newQty == 0) {
                lotsMapper.deleteZeroLot(lot.getLotId());
            }
        }

        if (remainingQty > 0) {
            throw new IllegalStateException("재고 부족: " + remainingQty + "개 출하 불가능");
        }

        // 4. 주문 상태를 출하 완료로 변경 (COMPLETE = 2)
        orderMapper.updateOrderStatus(orderId, 2);
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
