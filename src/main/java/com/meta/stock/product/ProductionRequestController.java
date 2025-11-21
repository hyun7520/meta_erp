package com.meta.stock.product;

import com.meta.stock.product.DTO.ProductionRequestDTO;
import com.meta.stock.product.Service.ProductionRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production-requests")
public class ProductionRequestController {

    private final ProductionRequestService productionRequestService;

    public ProductionRequestController(ProductionRequestService productionRequestService) {
        this.productionRequestService = productionRequestService;
    }

    // 1. 전체 주문 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductionRequestDTO.Response>> getAllOrders() {
        List<ProductionRequestDTO.Response> responses = productionRequestService.getAllOrders();
        return ResponseEntity.ok(responses);
    }

    // 2. 대기중 주문 목록 조회
    @GetMapping("/pending")
    public ResponseEntity<List<ProductionRequestDTO.Response>> getPendingOrders() {
        List<ProductionRequestDTO.Response> responses = productionRequestService.getPendingOrders();
        return ResponseEntity.ok(responses);
    }

    // 3. 생산시작 주문 목록 조회
    @GetMapping("/in-progress")
    public ResponseEntity<List<ProductionRequestDTO.Response>> getInProgressOrders() {
        List<ProductionRequestDTO.Response> responses = productionRequestService.getInProgressOrders();
        return ResponseEntity.ok(responses);
    }

    // 4. 생산완료 주문 목록 조회
    @GetMapping("/completed")
    public ResponseEntity<List<ProductionRequestDTO.Response>> getCompletedOrders() {
        List<ProductionRequestDTO.Response> responses = productionRequestService.getCompletedOrders();
        return ResponseEntity.ok(responses);
    }

    // 5. 인기 상품 조회 (최다 요청 제품)
    @GetMapping("/top-product")
    public ResponseEntity<ProductionRequestDTO.TopProduct> getTopProduct() {
        ProductionRequestDTO.TopProduct topProduct = productionRequestService.getTopProduct();
        if (topProduct == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topProduct);
    }



    // 7. 생산 요청 생성 (POST)
    @PostMapping
    public ResponseEntity<ProductionRequestDTO.Response> createProductionRequest(
            @RequestBody ProductionRequestDTO.Request request) {
        ProductionRequestDTO.Response response = productionRequestService.createProductionRequest(request);
        return ResponseEntity.ok(response);
    }

    // 8. 생산 시작 처리
    @PutMapping("/{prId}/start")
    public ResponseEntity<ProductionRequestDTO.Response> startProduction(@PathVariable long prId) {
        ProductionRequestDTO.Response response = productionRequestService.startProduction(prId);
        return ResponseEntity.ok(response);
    }

    // 9. 생산 완료 처리
    @PutMapping("/{prId}/complete")
    public ResponseEntity<ProductionRequestDTO.Response> completeProduction(@PathVariable long prId) {
        ProductionRequestDTO.Response response = productionRequestService.completeProduction(prId);
        return ResponseEntity.ok(response);
    }

    // 10. 생산 요청 삭제 (DELETE)
    @DeleteMapping("/{prId}")
    public ResponseEntity<Void> deleteProductionRequest(@PathVariable long prId) {
        productionRequestService.deleteProductionRequest(prId);
        return ResponseEntity.noContent().build();
    }

    // 11. 생산 완료 주문 일괄 삭제
    @DeleteMapping("/completed")
    public ResponseEntity<Void> deleteCompletedOrders() {
        productionRequestService.deleteCompletedOrders();
        return ResponseEntity.noContent().build();
    }
    // 12. 생산 요청 수정 (PUT)
    @PutMapping("/{prId}")
    public ResponseEntity<ProductionRequestDTO.Response> updateProductionRequest(
            @PathVariable long prId,
            @RequestBody ProductionRequestDTO.Request request) {
        ProductionRequestDTO.Response response = productionRequestService.updateProductionRequest(prId, request);
        return ResponseEntity.ok(response);
    }

    // 13. 단일 생산 요청 조회 (수정 모달용)
    @GetMapping("/{prId}")
    public ResponseEntity<ProductionRequestDTO.Response> getProductionRequest(@PathVariable long prId) {
        ProductionRequestDTO.Response response = productionRequestService.getProductionRequestById(prId);
        return ResponseEntity.ok(response);
    }
}