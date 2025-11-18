package com.meta.stock.product.controller;

import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.product.dto.OrderDto;
import com.meta.stock.product.service.OrderService;
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

    // 주문 조회, 검색 기능 추가할 것
    @GetMapping("order")
    public String getAllOrders(@RequestParam(defaultValue = "0") int keyword, Model model) {
        // keyword: 1 = complete, 0 = ongoing, -1 = failed
        List<OrderDto> orderRequests = orderService.findAllOrders(keyword);
        model.addAttribute("orders", orderRequests);
        return "order";
    }

    // 주문 클릭 시, 제품 출하 페이지로 이동
    @GetMapping("order/ship/{orderId}")
    public String shipItems(@PathVariable long orderId, Model model) {
        OrderDto orderDto = orderService.findOrderById(orderId);
        model.addAttribute("order", orderDto);

        // 주문 수량만큼 생산 시 필요한 원자재 계산
        List<MaterialRequirementDto> requirements = materialService.calculateRequiredMaterials(
                orderDto.getProductDto().getProductId(),
                orderDto.getQty()
        );

        // 현재 재고와 비교해서 부족 여부 판단
        requirements.forEach(req -> {
            int currentStock = materialService.getCurrentStock(req.getMaterialName());
            req.setCurrentStock(currentStock);
            req.setSufficient(currentStock >= req.getRequiredQty());
        });

        model.addAttribute("requirements", requirements);

        return "orderDetail";
    }

    // 주문서 작성 및 출하
    @PostMapping("order/ship/{orderId}")
    public String confirmShipment(@PathVariable int orderId,
                                  Model model,
                                  @Valid @ModelAttribute("orderDto") OrderDto orderDto,
                                  BindingResult br) {

        // 주문서 오류, 수량 부족 시 리다이렉트 하도록
        if(br.hasErrors()) {
            return "ship_form";
        }
        
        // 출하 확인 및 DB 반영
        return orderService.shipOrder(orderId);
    }
}
