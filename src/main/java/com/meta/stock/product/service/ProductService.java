package com.meta.stock.product.service;

import com.meta.stock.product.mapper.OrderMapper;
import com.meta.stock.product.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    @GetMapping("prod/items")
    public List<OrderDto> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    @GetMapping("prod/items/{productId}")
    public List<MaterialDto> getMaterialStatus(@PathVariable int productId) {
        return productService.getMaterialStatus(productId);
    }
}
