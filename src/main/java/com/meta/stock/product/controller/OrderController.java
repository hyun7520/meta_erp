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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // 주문 조회
    // 검색 기능 추가할 것
    @GetMapping("order")
    public String findAllOrders(@RequestParam(defaultValue = "0") int keyword, Model model) {
        // keywor - 0: 대기, 1: 수주, 2: 완료
        List<OrderDto> orderRequests = orderService.findAllOrders(keyword);
        model.addAttribute("orders", orderRequests);
        return "order";
    }

    @PostMapping("/order/accept/{orderId}")
    public String acceptOrder(@PathVariable long orderId) {
        orderService.acceptOrder(orderId);
        return "redirect:/order";
    }

    // 주문 클릭 시, 제품 상세 페이지로 이동
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
            requirements = orderService.calculateStock(requirements);
            model.addAttribute("requirements", requirements);

        } else if (orderDto.getComplete() == 1) {
            // 수주 완료 → 남은 필요 재료 계산 (출하된 수량 고려 가능, 여기선 단순 전체 필요량)
            List<MaterialRequirementDto> remaining = materialService.calculateRequiredMaterials(
                    orderDto.getProductDto().getProductId(), orderDto.getQty());
            remaining = orderService.calculateRemaining(remaining);
            model.addAttribute("remainingRequirements", remaining);
        }
        return "orderDetail";
    }

    // 주문서 작성 및 출하
    @PostMapping("order/ship/{orderId}")
    public String confirmShipment(@PathVariable long orderId,
                                  Model model,
                                  RedirectAttributes redirectAttrs) {

        // 제품 출하 - 물품 재고에서 주문 수량 만큼 주문 빼기
        // 로트 단위 별로 shelf_days가 임박한 순서부터
        // 로트 단위가 남을 경우 다음 같은 제품 출하 시 그 제품부터 나가게
        try {
            orderService.shipOrder(orderId);  // 성공 시
            redirectAttrs.addFlashAttribute("message", "출하 처리 완료되었습니다.");
        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/order";
    }
}
