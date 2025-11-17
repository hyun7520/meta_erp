package com.meta.stock.product.controller;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.product.dto.OrderDto;
import com.meta.stock.product.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RequestController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private OrderService orderService;

    // 이전에 요청한 재료 요청서 확인
    @GetMapping("prod/mat-req/{mrId}")
    public List<MaterialDto> getMaterialRequestDetails(@PathVariable int mrId) {
        return materialService.getMaterialRequestDetails(mrId);
    }

    // 선택한 제품에 대한 재료 요청서 작성 페이지로 이동
    // AI 추천에 승낙한 경우도 여기서 처리
    @GetMapping("prod/mat-req/order/{productId}")
    public String getMaterialRequestForm(@PathVariable(required = false) int productId,
                                         @RequestParam List<Integer> materials,
                                         Model model) {

        List<MaterialDto> materialList =  materialService.getMaterials(materials);
        model.addAttribute(materialList);

        return "material_request";
    }

    // 작성한 재료 발주 요청 정송 - DB 반영
    @PostMapping("prod/mat-req/order/{productId}")
    public String sendMaterialRequest(@Valid @ModelAttribute("materialDto") MaterialDto dto,
                                      BindingResult br,
                                      @RequestParam int productId) {

        if(br.hasErrors()){
            // return form
        }
        // DB 반영 성공 여부 return
        return materialService.addRequest();
    }

    // 전달받은 주문서 확인 - 페이지 이동
    @GetMapping("prod/ord-req/{orderId}")
    public String confirmOrder(@PathVariable int orderId) {
        
        return null;
    }
}
