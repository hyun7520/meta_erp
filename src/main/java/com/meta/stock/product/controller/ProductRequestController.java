package com.meta.stock.product.controller;

import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.product.dto.ProductRequestDto;
import com.meta.stock.product.dto.ProductionRequestDTO;
import com.meta.stock.product.service.ProductionRequestService;
import com.meta.stock.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductRequestController {

    @Autowired
    private ProductionRequestService productionRequestService;
    @Autowired
    private ProductService productService;

    // 주문 조회
    @GetMapping("/pr")
    public String getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "prId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir,
            Model model) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy));

        Page<ProductRequestDto> productRequests =
                productionRequestService.findAllProductRequests(keyword, pageable);

        Map<String, Integer> statusStats = productionRequestService.getStatusStatistics();

        model.addAttribute("statusStats", statusStats);
        model.addAttribute("productRequests", productRequests);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);

        return "productionRequests";
    }

    @GetMapping("/pr/{id}")
    public String getProductRequestDetail(@PathVariable Long id, Model model) {
        ProductRequestDto productRequest = productionRequestService.findProductRequestById(id);

        // 재고 및 요구사항 계산
        int currentProductStock = productService.getCurrentProductStock(productRequest.getSerialCode());
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
                model.addAttribute("requirements", remainingRequirements);
            }
        }

        model.addAttribute("productRequest", productRequest);
        model.addAttribute("currentProductStock", currentProductStock);

        return "productionRequestsDetail :: content";
    }

    // 주문수주
    @PostMapping("/pr/accept/{prId}")
    public String beginProduction(@PathVariable long prId) {
        productionRequestService.acceptProductionRequest(prId);
        return "redirect:/pr";
    }

    // 제품 출하
    @PostMapping("/pr/ship/{id}")
    public String shipOrder(@PathVariable Long id) {
        productionRequestService.shipOrder(id);
        return "redirect:/pr";
    }

    // 1. 전체 주문 목록 조회
    @GetMapping("/pro")
    public ResponseEntity<List<ProductionRequestDTO.Response>> getAllOrders() {
        List<ProductionRequestDTO.Response> responses = productionRequestService.getAllOrders();
        return ResponseEntity.ok(responses);
    }

    // 2. 대기중 주문 목록 조회
    @GetMapping("/pro/pending")
    public ResponseEntity<List<ProductionRequestDTO.Response>> getPendingOrders() {
        List<ProductionRequestDTO.Response> responses = productionRequestService.getPendingOrders();
        return ResponseEntity.ok(responses);
    }

    // 3. 생산시작 주문 목록 조회
    @GetMapping("/pro/in-progress")
    public ResponseEntity<List<ProductionRequestDTO.Response>> getInProgressOrders() {
        List<ProductionRequestDTO.Response> responses = productionRequestService.getInProgressOrders();
        return ResponseEntity.ok(responses);
    }

    // 4. 생산완료 주문 목록 조회
    @GetMapping("/pro/completed")
    public ResponseEntity<List<ProductionRequestDTO.Response>> getCompletedOrders() {
        List<ProductionRequestDTO.Response> responses = productionRequestService.getCompletedOrders();
        return ResponseEntity.ok(responses);
    }

    // 5. 인기 상품 조회 (최다 요청 제품)
    @GetMapping("/pro/top-product")
    public ResponseEntity<ProductionRequestDTO.TopProduct> getTopProduct() {
        ProductionRequestDTO.TopProduct topProduct = productionRequestService.getTopProduct();
        if (topProduct == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topProduct);
    }

    // 7. 생산 요청 생성 (POST)
    @PostMapping("/pro")
    public ResponseEntity<ProductionRequestDTO.Response> createProductionRequest(
            @RequestBody ProductionRequestDTO.Request request) {
        ProductionRequestDTO.Response response = productionRequestService.createProductionRequest(request);
        return ResponseEntity.ok(response);
    }

    // 8. 생산 시작 처리
    @PutMapping("/pro/{prId}/start")
    public ResponseEntity<ProductionRequestDTO.Response> startProduction(@PathVariable long prId) {
        ProductionRequestDTO.Response response = productionRequestService.startProduction(prId);
        return ResponseEntity.ok(response);
    }

    // 9. 생산 완료 처리
    @PutMapping("/pro/{prId}/complete")
    public ResponseEntity<ProductionRequestDTO.Response> completeProduction(@PathVariable long prId) {
        ProductionRequestDTO.Response response = productionRequestService.completeProduction(prId);
        return ResponseEntity.ok(response);
    }

    // 10. 생산 요청 삭제 (DELETE)
    @DeleteMapping("/pro/{prId}")
    public ResponseEntity<Void> deleteProductionRequest(@PathVariable long prId) {
        productionRequestService.deleteProductionRequest(prId);
        return ResponseEntity.noContent().build();
    }

    // 11. 생산 완료 주문 일괄 삭제
    @DeleteMapping("/pro/completed")
    public ResponseEntity<Void> deleteCompletedOrders() {
        productionRequestService.deleteCompletedOrders();
        return ResponseEntity.noContent().build();
    }
    // 12. 생산 요청 수정 (PUT)
    @PutMapping("/pro/{prId}")
    public ResponseEntity<ProductionRequestDTO.Response> updateProductionRequest(
            @PathVariable long prId,
            @RequestBody ProductionRequestDTO.Request request) {
        ProductionRequestDTO.Response response = productionRequestService.updateProductionRequest(prId, request);
        return ResponseEntity.ok(response);
    }

    // 13. 단일 생산 요청 조회 (수정 모달용)
    @GetMapping("/pro/{prId}")
    public ResponseEntity<ProductionRequestDTO.Response> getProductionRequest(@PathVariable long prId) {
        ProductionRequestDTO.Response response = productionRequestService.getProductionRequestById(prId);
        return ResponseEntity.ok(response);
    }
}
