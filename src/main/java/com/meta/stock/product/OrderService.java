package com.meta.stock.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // 생성자
    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // 전체 주문 조회
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 미완료 주문 조회
    public List<OrderDTO> getIncompleteOrders() {
        return orderRepository.findByComplete(0)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 주문 생성
    @Transactional
    public OrderDTO createOrder(OrderCreateDTO dto) {
        ProductsEntity product = productRepository.findById(Long.valueOf(dto.getProductId()))
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다."));

        OrderEntity order = new OrderEntity();
        order.setProductId(dto.getProductId());
        order.setRequestBy(dto.getRequestBy());
        order.setQty(dto.getQty());
        order.setUnit(dto.getUnit());
        order.setRequestDate(dto.getRequestDate());
        order.setDeadline(dto.getDeadline());
        order.setComplete(0); // 미완료

        OrderEntity saved = orderRepository.save(order);
        return convertToDTO(saved);
    }

    // Entity -> DTO 변환
    private OrderDTO convertToDTO(OrderEntity entity) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(entity.getOrderId());
        dto.setRequestBy(entity.getRequestBy());
        dto.setQty(entity.getQty());
        dto.setUnit(entity.getUnit());
        dto.setRequestDate(entity.getRequestDate());
        dto.setDeadline(entity.getDeadline());
        dto.setComplete(entity.getComplete());

        // 제품 이름 조회
        if (entity.getProductId() != 0) {
            productRepository.findById(Long.valueOf(entity.getProductId()))
                    .ifPresent(product -> dto.setProductName(product.getProductName()));
        }

        // 상태 계산
        if (entity.getComplete() == 1) {
            dto.setStatus("완료");
        } else {
            dto.setStatus("진행중");
        }

        return dto;
    }
}