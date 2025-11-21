package com.meta.stock.product.controller;

import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.product.dto.ProductDTO;
import com.meta.stock.product.dto.ProductListDTO;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    @GetMapping("product")
    public String getAllProducts(
            @RequestParam(defaultValue = "0") int stockPage,
            @RequestParam(defaultValue = "5") int stockSize,
            @RequestParam(required = false) String stockKeyword,
            @RequestParam(defaultValue = "storageDate") String stockSortBy,
            @RequestParam(defaultValue = "DESC") String stockSortDir,

            @RequestParam(defaultValue = "0") int prPage,
            @RequestParam(defaultValue = "5") int prSize,
            @RequestParam(required = false) String prKeyword,
            @RequestParam(defaultValue = "requestDate") String prSortBy,
            @RequestParam(defaultValue = "ASC") String prSortDir,

            @RequestParam(defaultValue = "0") int mrPage,
            @RequestParam(defaultValue = "5") int mrSize,
            @RequestParam(required = false) String mrKeyword,
            @RequestParam(defaultValue = "requestDate") String mrSortBy,
            @RequestParam(defaultValue = "ASC") String mrSortDir,

            Model model) {

        Pageable stockPageable = PageRequest.of(stockPage, stockSize, Sort.by(Sort.Direction.fromString(stockSortDir), stockSortBy));
        Page<ProductStockDto> totalProductStock = productService.findTotalProductStock(stockKeyword, stockPageable);
        model.addAttribute("totalProductStock", totalProductStock);
        model.addAttribute("stockKeyword", stockKeyword);
        model.addAttribute("stockSortBy", stockSortBy);
        model.addAttribute("stockSortDir", stockSortDir);

        Pageable prPageable = PageRequest.of(prPage, prSize, Sort.by(Sort.Direction.fromString(prSortDir), prSortBy));
        Page<ProductRequestDto> productRequests = productionRequestService.findOngoingProductRequests(prKeyword, prPageable);
        model.addAttribute("productRequests", productRequests);
        model.addAttribute("prKeyword", prKeyword);
        model.addAttribute("prSortBy", prSortBy);
        model.addAttribute("prSortDir", prSortDir);

        Pageable mrPageable = PageRequest.of(mrPage, mrSize, Sort.by(Sort.Direction.fromString(mrSortDir), mrSortBy));
        Page<MaterialRequestDto> materialRequests = materialService.findOngoingMaterialRequests(mrKeyword, mrPageable);
        model.addAttribute("materialRequests", materialRequests);
        model.addAttribute("mrKeyword", mrKeyword);
        model.addAttribute("mrSortBy", mrSortBy);
        model.addAttribute("mrSortDir", mrSortDir);

        return "productionMain";
    }

    //  제품 목록 조회 API - order.html에서 사용
    @GetMapping("/products")
    public ResponseEntity<List<ProductListDTO>> getProductsList() {
        List<ProductListDTO> responses = productService.getProductsForOrder();
        return ResponseEntity.ok(responses);
    }
}