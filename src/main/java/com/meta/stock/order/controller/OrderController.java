package com.meta.stock.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("/order")
    public String goOrder() {
        return "order/order";
    }

    @GetMapping("/request")
    public String goMaterialRequest() {
        return "order/material_request";
    }
}
