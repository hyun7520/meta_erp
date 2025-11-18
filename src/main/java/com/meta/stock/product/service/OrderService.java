package com.meta.stock.product.service;

import com.meta.stock.product.dto.OrderDto;
import com.meta.stock.product.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("prod/items")
    public List<OrderDto> getAllOrders(String status) {
        return orderMapper.getAllOrders(status);
    }

    public OrderDto findByOrder(int orderId) {
        return orderMapper.getOrderById(orderId);
    }

    public String shipOrder(int orderId) {
        return null;
    }
}
