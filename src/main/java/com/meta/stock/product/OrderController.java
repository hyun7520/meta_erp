package com.meta.stock.product;

import com.meta.stock.product.DTO.OrderCreateDTO;
import com.meta.stock.product.DTO.OrderDTO;
import com.meta.stock.product.DTO.TopProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // 생성자
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 전체 주문 조회
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // 미완료 주문 조회
    @GetMapping("/incomplete")
    public ResponseEntity<List<OrderDTO>> getIncompleteOrders() {
        return ResponseEntity.ok(orderService.getIncompleteOrders());
    }

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderCreateDTO dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }
    // 최근 한 달간 가장 많이 주문된 상품 조회
    @GetMapping("/top-product")
    public ResponseEntity<TopProductDTO> getTopProduct() {
        TopProductDTO topProduct = orderService.getTopProductLastMonth();
        if (topProduct == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topProduct);
    }
}