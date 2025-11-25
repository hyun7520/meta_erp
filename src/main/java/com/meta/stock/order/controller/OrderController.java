package com.meta.stock.order.controller;

import com.meta.stock.user.employees.dto.EmployeeGetDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("/order")
    public String goOrder(HttpSession session) {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        } else {
            EmployeeGetDto employee = (EmployeeGetDto) session.getAttribute("employee");
            if (employee.getDepartment().equals("생산") || employee.getRole().equals("사원")) {
                return "redirect:/dash";
            }
        }
        return "order/order";
    }

    @GetMapping("/request")
    public String goMaterialRequest(HttpSession session) {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        } else {
            EmployeeGetDto employee = (EmployeeGetDto) session.getAttribute("employee");
            if (employee.getDepartment().equals("생산") || employee.getRole().equals("사원")) {
                return "redirect:/dash";
            }
        }
        return "order/material_request";
    }
}
