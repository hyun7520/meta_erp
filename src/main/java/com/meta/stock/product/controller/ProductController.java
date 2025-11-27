package com.meta.stock.product.controller;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.product.dto.*;
import com.meta.stock.product.service.ProductionRequestService;
import com.meta.stock.product.service.PredictService;
import com.meta.stock.product.service.ProductService;
import com.meta.stock.python.PythonService;
import com.meta.stock.user.employees.dto.EmployeeGetDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 제품과 연관된 기능 수행 컨트롤러
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private ProductionRequestService productionRequestService;
    @Autowired
    private PythonService pythonService;

    private final int limit = 3;

    // 생산 페이지 로드
    @GetMapping("/product")
    public String getProductsDash(HttpSession session) throws IOException {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        } else {
            EmployeeGetDto employee = (EmployeeGetDto) session.getAttribute("employee");
            if (employee.getDepartment().equals("경영") || employee.getRole().equals("사원")) {
                return "redirect:/dash";
            }
        }

        pythonService.runPythonScript();
        return "product/productionMain";
    }

    // 완제품 재고 전체 조회 페이지
    @GetMapping("/product/stock")
    public String getProductStockList(HttpSession session) {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        } else {
            EmployeeGetDto employee = (EmployeeGetDto) session.getAttribute("employee");
            if (employee.getDepartment().equals("경영") || employee.getRole().equals("사원")) {
                return "redirect:/dash";
            }
        }
        return "product/productionStocks";
    }

    @GetMapping("/product/form")
    public String getProductionForm(Model model, HttpSession session) {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        } else {
            EmployeeGetDto employee = (EmployeeGetDto) session.getAttribute("employee");
            if (employee.getDepartment().equals("경영") || employee.getRole().equals("사원")) {
                return "redirect:/dash";
            }
        }

        // 제품 이름과 제품 재고 수량 조회
        List<FixedProductDto> fpDto = productService.getFixedProductWithStockQty();

        // 제품별 재료 조회
        for(FixedProductDto dto: fpDto) {
            List<MaterialDto> requiredMaterials = productService.getRequiredMaterials(dto.getFpId());
            dto.setRequiredMaterials(requiredMaterials);
        }
        model.addAttribute("fpDto", fpDto);

        return "product/productionForm";
    }

    @GetMapping("/product/ongoing")
    public ResponseEntity<Map<String, Object>> getProductsOngoing(
            @RequestParam(defaultValue = "0") int prPage,
            @RequestParam(defaultValue = "10") int prSize,
            @RequestParam(required = false) String prKeyword,
            @RequestParam(defaultValue = "requestDate") String prSortBy,
            @RequestParam(defaultValue = "ASC") String prSortDir
    ) {
        // 진행 중인 생산 요청 페이징/검색/정렬
        Pageable prPageable = PageRequest.of(prPage, prSize, Sort.by(Sort.Direction.fromString(prSortDir), prSortBy));
        Page<ProductRequestDto> productRequests = productionRequestService.findOngoingRequestsWithPaging(prKeyword, prPageable);

        Map<String, Object> result = new HashMap<>();
        result.put("products", productRequests);
        result.put("prKeyword", prKeyword);
        result.put("prSortBy", prSortBy);
        result.put("prSortDir", prSortDir);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/product/progress")
    public ResponseEntity<List<ProductStockDto>> getProductsProgress() {
        // 모든 완제품 재고 현황 조회
        List<ProductStockDto> allProducts = productService.findTotalProductStock();
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/product/readyMaterials")
    public ResponseEntity<Map<String, Object>> getProductsReadyMaterials(
            // 재료 발주 파라미터
            @RequestParam(defaultValue = "0") int mrPage,
            @RequestParam(defaultValue = "5") int mrSize,
            @RequestParam(required = false) String mrKeyword,
            @RequestParam(defaultValue = "requestDate") String mrSortBy,
            @RequestParam(defaultValue = "ASC") String mrSortDir
    ) {
        // 미승인 재료 발주 요청 페이징/검색/정렬
        Pageable mrPageable = PageRequest.of(mrPage, mrSize, Sort.by(Sort.Direction.fromString(mrSortDir), mrSortBy));
        Page<MaterialRequestDto> materialRequests = materialService.findMaterialRequestsWithPaging(mrKeyword, mrPageable);

        Map<String, Object> result = new HashMap<>();
        result.put("materials", materialRequests);
        result.put("mrKeyword", mrKeyword);
        result.put("mrSortBy", mrSortBy);
        result.put("mrSortDir", mrSortDir);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/product/stock/list")
    public ResponseEntity<Map<String, Object>> getProductStockList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "storageDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy));

        // 로트별 상세 재고 조회
        Page<ProductStockDto> productStock =
                productService.findProductStockWithPaging(keyword, pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("stocks", productStock);
        result.put("keyword", keyword);
        result.put("sortBy", sortBy);
        result.put("sortDir", sortDir);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/product")
    public String beginProduction(Model model,
                                  @RequestParam List<Long> fpIds,
                                  @RequestParam List<Integer> quantities){

        for (int i = 0; i < fpIds.size(); i++) {
            Long fpId = fpIds.get(i);
            Integer qty = quantities.get(i);

            // 랜덤 제품 로스값 삽입
            // double lossRate = Math.random() * 0.15;  // 0 ~ 15%

            // 최소 0개, 최대 qty-1개까지 손실 (수량이 1개면 손실 0 보장)
            // int loss = (int) Math.round(qty * lossRate);
            // loss = Math.max(0, Math.min(loss, qty - 1));

            // 모델로 loss율을 가져온다면 적용할 곳
            // int loss = productService.getProductionLoss(fpId);

            // 수량이 0보다 큰 것만 생산
            if (qty != null && qty > 0) {
                productService.produceProduct(fpId, qty);
                materialService.decreaseMaterial(fpId, qty);
            }
        }

        return "redirect:/product";
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO.Response>> getAllProducts() {
        List<ProductDTO.Response> responses = productService.getAllProducts();
        return ResponseEntity.ok(responses);
    }

    //  제품 목록 조회 API - order.html에서 사용
    @GetMapping("/products")
    public ResponseEntity<List<ProductListDTO>> getProductsList() {
        List<ProductListDTO> responses = productService.getProductsForOrder();
        return ResponseEntity.ok(responses);
    }
}