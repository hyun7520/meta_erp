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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @GetMapping("/pr")
    public String getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "prId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir,
            Model model) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy));

        Page<ProductRequestDto> productRequests =
                productionRequestService.findAllProductRequests(keyword, pageable);

        model.addAttribute("productRequests", productRequests);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);

        return "productionRequests";
    }

    // 주문 클릭 시, 제품 상세 페이지로 이동
    @GetMapping("/pr/{id}")
    public String getProductionRequestDetail(@PathVariable Long id, Model model) {
        // 1. 생산 요청 기본 정보 조회
        ProductRequestDto productRequest = productionRequestService.findProductRequestById(id);
        model.addAttribute("productRequest", productRequest);

        // 2. 현재 완제품 재고 조회
        int currentProductStock = productService.getCurrentProductStock(productRequest.getSerialCode());
        model.addAttribute("currentProductStock", currentProductStock);

        // 3. 상태에 따른 추가 정보 조회
        if (productRequest.getProductionStartDate() == null) {
            // 미수주 상태: 수주 시 필요한 원자재 계산
            List<MaterialRequirementDto> requirements =
                    productService.calculateMaterialRequirements(productRequest.getSerialCode(), productRequest.getTargetQty());
            model.addAttribute("requirements", requirements);

        } else if (productRequest.getEndDate() == null) {
            // 수주 완료 상태: 남은 생산을 위한 원자재 현황
            int remainingQty = productRequest.getTargetQty() - currentProductStock;
            if (remainingQty > 0) {
                List<MaterialRequirementDto> remainingRequirements =
                        productService.calculateMaterialRequirements(productRequest.getSerialCode(), remainingQty);
                model.addAttribute("remainingRequirements", remainingRequirements);
            }
        }

        return "productionRequestsDetail";
    }

    // 생산 확인
    @PostMapping("/pr/accept/{prId}")
    public String acceptOrder(@PathVariable long prId) {
        productionRequestService.acceptProductionRequest(prId);
        return "redirect:/order";
    }

    // 주문 수주
    @PostMapping("/order/accept/{id}")
    public String acceptOrder(@PathVariable Long id) {
        productionRequestService.acceptOrder(id);
        return "redirect:/pr/" + id;
    }

    // 제품 출하
    @PostMapping("/order/ship/{id}")
    public String shipOrder(@PathVariable Long id) {
        productionRequestService.shipOrder(id);
        return "redirect:/order";
    }

}
