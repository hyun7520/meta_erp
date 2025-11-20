package com.meta.stock.product.controller;

import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.product.dto.ProductDto;
import com.meta.stock.product.dto.ProductRequestDto;
import com.meta.stock.product.dto.ProductStockDto;
import com.meta.stock.product.service.ProductionRequestService;
import com.meta.stock.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductRequestController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private ProductionRequestService productionRequestService;
    @Autowired
    private ProductService productService;

    // 주문 조회
    // 검색 기능 추가할 것
    @GetMapping("pr")
    public String findAllRequests(Model model) {
        // keyword - 0: 대기, 1: 수주, 2: 완료
        List<ProductRequestDto> productRequests = productionRequestService.findAllProductionRequests();
        model.addAttribute("productRequests", productRequests);
        return "order";
    }

    // 생산 확인
    @PostMapping("/pr/accept/{prId}")
    public String acceptOrder(@PathVariable long prId) {
        productionRequestService.acceptProductionRequest(prId);
        return "redirect:/order";
    }

    // 주문 클릭 시, 제품 상세 페이지로 이동
    @GetMapping("pr/{prId}")
    public String shipItems(@PathVariable long prId, Model model) {
        ProductRequestDto productRequestDto = productionRequestService.findProductRequestById(prId);
        List<ProductDto> products = productService.getProductsByPRId(prId);
        model.addAttribute("ProductRequest", productRequestDto);
        model.addAttribute("products", products);

        int currentProductStock = productService.getCurrentProductStock(productRequestDto.getPrId());
        model.addAttribute("currentProductStock", currentProductStock);

        if (productRequestDto.getProductionStartDate() == null) {
            // productionStartDate == null: 아직 제조를 시작하지 않았다(미수주)
            // 로스율에 따른 계획 생산량을 계산식이 필요
            List<MaterialRequirementDto> requirements = materialService.calculateRequiredMaterials(
                    products.get(0).getProductId(), productRequestDto.getTargetQty());
            requirements = productionRequestService.calculateStock(requirements);
            model.addAttribute("requirements", requirements);

        } else if (productRequestDto.getProductionStartDate() != null) {
            // 수주를 한 경우 → 남은 필요 재료 계산
            List<MaterialRequirementDto> remaining = materialService.calculateRequiredMaterials(
                    products.get(0).getProductId(), productRequestDto.getCompletedQty() - productRequestDto.getPlannedQty());
            remaining = productionRequestService.calculateRemaining(remaining);
            model.addAttribute("remainingRequirements", remaining);
        }
        return "orderDetail";
    }

    // 주문서 작성 및 출하
    @PostMapping("pr/ship/{orderId}")
    public String confirmShipment(@PathVariable long prId,
                                  Model model,
                                  RedirectAttributes redirectAttrs) {

        // 제품 출하 - 물품 재고에서 주문 수량 만큼 주문 빼기
        // 로트 단위 별로 shelf_days가 임박한 순서부터
        // 로트 단위가 남을 경우 다음 같은 제품 출하 시 그 제품부터 나가게
        try {
            productionRequestService.shipOrder(prId);  // 성공 시
            redirectAttrs.addFlashAttribute("message", "출하 처리 완료되었습니다.");
        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/order";
    }

    @GetMapping("pr/product")
    public String productForm(Model model) {

        List<ProductStockDto> curTotalStock = productService.findAllProductStockByProduct();
        List<ProductStockDto> totalRequiredStock = productService.findTotalRequiredStock();
        model.addAttribute("curTotalStock", curTotalStock);
        model.addAttribute("totalRequiredStock", totalRequiredStock);

        return "production";
    }
}
