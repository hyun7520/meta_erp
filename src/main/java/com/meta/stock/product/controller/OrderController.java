package com.meta.stock.product.controller;

import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.product.dto.OrderDto;
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
    @Autowired
    private ProductService productService;

    // 주문 조회, 검색 기능 추가할 것
    @GetMapping("order")
    public String getAllOrders(@RequestParam(defaultValue = "0") int keyword, Model model) {
        // keyword: 1 = complete, 0 = ongoing, -1 = failed
        List<OrderDto> orderRequests = orderService.findAllOrders(keyword);
        model.addAttribute("orders", orderRequests);
        return "order";
    }

    // 주문 클릭 시, 제품 출하 페이지로 이동
    @GetMapping("order/{orderId}")
    public String shipItems(@PathVariable long orderId, Model model) {
        OrderDto orderDto = orderService.findOrderById(orderId);
        model.addAttribute("order", orderDto);

        int currentProductStock = productService.getCurrentProductStock(orderDto.getProductDto().getProductId());
        model.addAttribute("currentProductStock", currentProductStock);

        if (orderDto.getComplete() == 0) {
            // 미수주 → 전체 필요 재료 계산
            List<MaterialRequirementDto> requirements = materialService.calculateRequiredMaterials(
                    orderDto.getProductDto().getProductId(), orderDto.getQty());
            requirements.forEach(req -> {
                int stock = materialService.getCurrentStock(req.getMaterialName());
                req.setCurrentStock(stock);
                req.setSufficient(stock >= req.getRequiredQty());
            });
            model.addAttribute("requirements", requirements);

        } else if (orderDto.getComplete() == 1) {
            // 수주 완료 → 남은 필요 재료 계산 (출하된 수량 고려 가능, 여기선 단순 전체 필요량)
            List<MaterialRequirementDto> remaining = materialService.calculateRequiredMaterials(
                    orderDto.getProductDto().getProductId(), orderDto.getQty());

            remaining.forEach(req -> {
                int stock = materialService.getCurrentStock(req.getMaterialName());
                req.setCurrentStock(stock);
                req.setRemainingQty(Math.max(0, req.getRequiredQty() - stock));
            });
            model.addAttribute("remainingRequirements", remaining);
        }

        return "orderDetail";
    }

    // 주문서 작성 및 출하
    @PostMapping("order/ship/{orderId}")
    public String confirmShipment(@PathVariable int orderId,
                                  Model model) {

        // 주문서 오류, 수량 부족 시 리다이렉트 하도록

        
        // 출하 확인 및 DB 반영
        return orderService.shipOrder(orderId);
    }
}
