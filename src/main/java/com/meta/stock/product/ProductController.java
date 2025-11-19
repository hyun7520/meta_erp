package com.meta.stock.product;

import com.meta.stock.product.DTO.ProductDTO;
import com.meta.stock.product.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 생성자
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 전체 제품 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductDTO.Response>> getAllProducts() {
        List<ProductDTO.Response> responses = productService.getAllProducts();
        return ResponseEntity.ok(responses);
    }
}