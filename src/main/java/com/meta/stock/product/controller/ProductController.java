package com.meta.stock.product.controller;

import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.product.dto.OrderDto;
import com.meta.stock.product.dto.ProductDto;
import com.meta.stock.product.service.OrderService;
import com.meta.stock.product.service.PredictService;
import com.meta.stock.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// 제품과 연관된 기능 수행 컨트롤러
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PredictService predictService;

    // 생산 페이지 로드
    @GetMapping("product")
    public String getAllProducts(Model model) {
        // lots 테이블 join products table by lot 번호
        List<ProductDto> foundProducts = productService.getAllProducts();
        // 전체 주문 조회 - 기본 조건(현재 진행중인 경우만)
        List<OrderDto> foundOrders = orderService.getAllOrders("ongoing");
        // 재료 요청 라스트 조회
        List<MaterialRequestDto> materialRequests = materialService.getMaterialRequests();
        // 페이지 로드 시 예측 모델 호출 부족한 재고가 있는지 확인
        // return 값 고민해보기
        // List<PredictionDto> prediction = predictService.doPrediction();

        model.addAttribute("foundProducts", foundProducts);
        model.addAttribute("foundOrders", foundOrders);
        model.addAttribute("materialRequests", materialRequests);
        // model.addAttribute("prediction", prediction);

        return "product_main";
    }
}
