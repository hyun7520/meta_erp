package com.meta.stock.product.controller;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.product.dto.OrderDto;
import com.meta.stock.product.dto.ProductDto;
import com.meta.stock.product.service.OrderService;
import com.meta.stock.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private OrderService orderService;

    // 전체 주문 조회
    @GetMapping("order")
    public String getAllOrders(@RequestParam(defaultValue = "ongoing") String keyword) {
        // keyword: complete, ongoing, failed
        List<OrderDto> orderRequests = orderService.getAllOrders(keyword);
        return null;
    }

    // 주문 클릭 시, 제품 출하 페이지로 이동
    @GetMapping("product/ship/{orderId}")
    public String shipItems(@PathVariable int orderId) {

        OrderDto foundOrder = orderService.findByOrder(orderId);

        return "ship_form";
    }

    // 주문서 작성 및 출하
    @PostMapping("product/ship/{orderId}")
    public String confirmShipment(@PathVariable int orderId,
                                  @Valid @ModelAttribute("orderDto") OrderDto orderDto,
                                  BindingResult br) {

        // 주문서 오류, 수량 부족 시 리다이렉트 하도록
        if(br.hasErrors()) {
            return null;
        }
        
        // 출하 확인 및 DB 반영
        return orderService.shipOrder(orderId);
    }
}
