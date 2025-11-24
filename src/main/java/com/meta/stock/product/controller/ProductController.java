package com.meta.stock.product.controller;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.product.dto.FixedProductDto;
import com.meta.stock.product.dto.ProductDTO;
import com.meta.stock.product.dto.ProductRequestDto;
import com.meta.stock.product.dto.ProductStockDto;
import com.meta.stock.product.service.ProductionRequestService;
import com.meta.stock.product.service.PredictService;
import com.meta.stock.product.service.ProductService;
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

import java.util.List;

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
    private PredictService predictService;

    private final int limit = 3;

    // 생산 페이지 로드
    @GetMapping("/product")
    public String getAllProducts(

            // 생산 요청 파라미터
            @RequestParam(defaultValue = "0") int prPage,
            @RequestParam(defaultValue = "5") int prSize,
            @RequestParam(required = false) String prKeyword,
            @RequestParam(defaultValue = "requestDate") String prSortBy,
            @RequestParam(defaultValue = "ASC") String prSortDir,

            // 재료 발주 파라미터
            @RequestParam(defaultValue = "0") int mrPage,
            @RequestParam(defaultValue = "5") int mrSize,
            @RequestParam(required = false) String mrKeyword,
            @RequestParam(defaultValue = "requestDate") String mrSortBy,
            @RequestParam(defaultValue = "ASC") String mrSortDir,

            Model model) {


        List<ProductStockDto> allProducts = productService.getAllProductsStock();
        model.addAttribute("allProducts", allProducts);

        // 진행 중인 생산 요청 페이징/검색/정렬
        Pageable prPageable = PageRequest.of(prPage, prSize, Sort.by(Sort.Direction.fromString(prSortDir), prSortBy));
        Page<ProductRequestDto> productRequests = productionRequestService.findOngoingProductRequests(prKeyword, prPageable);
        model.addAttribute("productRequests", productRequests);
        model.addAttribute("prKeyword", prKeyword);
        model.addAttribute("prSortBy", prSortBy);
        model.addAttribute("prSortDir", prSortDir);

        // 미승인 재료 발주 요청 페이징/검색/정렬
        Pageable mrPageable = PageRequest.of(mrPage, mrSize, Sort.by(Sort.Direction.fromString(mrSortDir), mrSortBy));
        Page<MaterialRequestDto> materialRequests = materialService.findOngoingMaterialRequests(mrKeyword, mrPageable);
        model.addAttribute("materialRequests", materialRequests);
        model.addAttribute("mrKeyword", mrKeyword);
        model.addAttribute("mrSortBy", mrSortBy);
        model.addAttribute("mrSortDir", mrSortDir);

        // 페이지 로드 시 예측 모델 호출 부족한 재고가 있는지 확인
        // List<PredictionDto> prediction = predictService.doPrediction();
        // model.addAttribute("prediction", prediction);

        return "productionMain";
    }

    // 완제품 재고 전체 조회 페이지
    @GetMapping("/product/stock")
    public String getProductStockList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "storageDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            Model model) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy));

        // 로트별 상세 재고 조회 (기존 방식)
        Page<ProductStockDto> productStock =
                productService.findTotalProductStock(keyword, pageable);

        model.addAttribute("productStock", productStock);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "productionStocks";
    }

    @GetMapping("/product/form")
    public String getProductionForm(Model model) {
        
        // 제품 이름과 제품 재고 수량 조회
        List<FixedProductDto> fpDto = productService.getFixedProductWithStockQty();
        
        // 제품별 재료 조회
        for(FixedProductDto dto: fpDto) {
            List<MaterialDto> requiredMaterials = productService.getRequiredMaterials(dto.getFpId());
            dto.setRequiredMaterials(requiredMaterials);
        }
        model.addAttribute("fpDto", fpDto);

        return "productionForm";
    }

    @PostMapping("/product")
    public String beginProduction(Model model,
                                  @RequestParam List<Long> fpIds,
                                  @RequestParam List<Integer> quantities) {

        for (int i = 0; i < fpIds.size(); i++) {
            Long fpId = fpIds.get(i);
            Integer qty = quantities.get(i);

            int loss = (int) (Math.random() * 8) + 1;

            // 수량이 0보다 큰 것만 생산
            if (qty != null && qty > 0) {
                System.out.println("  → 제품 ID: " + fpId + ", 수량: " + qty);
                productService.produceProduct(fpId, qty);
            }
        }

        return "redirect:/product";
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO.Response>> getAllProducts() {
        List<ProductDTO.Response> responses = productService.getAllProducts();
        return ResponseEntity.ok(responses);
    }
}
