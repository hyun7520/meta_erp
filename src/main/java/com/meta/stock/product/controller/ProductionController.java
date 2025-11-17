package com.meta.stock.product.controller;

import com.meta.stock.product.dto.OrderDto;
import com.meta.stock.product.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductionController {

    @Autowired
    private ProductService productService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private OrderService orderService;

    @GetMapping("prod")
    public String getAllOrders() {
        List<OrderDto> allOrders = productService.getAllProductions();
        List<MaterialRequestDto> materialRequests = materialService.getAllMaterialRequest();
        List<OrderRequestDto> orderRequests = orderService.getAllOrders();
        return null;
    }

    // 수요와 재고 예측 API
    @GetMapping("prod/pred")
    public String predictDemand(@PathVariable)

    // 재고 예측 API
    @GetMapping("prod/{item}")
    public String predictStock(@PathVariable(required = false) int item) {
        return null;
    }
}
