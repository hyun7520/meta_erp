package com.meta.stock.product.controller;

import com.meta.stock.product.dto.OrderDto;
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

    @GetMapping("prod/mat-req/{mrId}")
    public List<MaterialDto> getMaterialRequestDetails(@PathVariable int mrId) {
        return materialService.getMaterialRequestDetails(mrId);
    }

    @GetMapping("prod/mat-req/order/{productId}")
    public String getMaterialRequestForm(@PathVariable int productId,
                                         @RequestParam List<Integer> materials,
                                         Model model) {

        List<MaterialDto> materialList =  materialService.getMaterials(materials);
        model.addAttribute(materialList);

        return "material_request";
    }

    @PostMapping("prod/mat-req/order/{productId}")
    public String sendMaterialRequest(@Valid @ModelAttribute("materialDto") MaterialDto dto,
                                      BindingResult br,
                                      @RequestParam int productId) {

        if(br.hasErrors()){
            // return form
        }
        // DB 반영 성공 여부 return
        return matarialSerivce.addRequest();
    }

    @GetMapping("prod/ord-req/{orderId}")
    public String confirmOrder(@Valid @ModelAttribute("orderDto") OrderDto orderDto,
                               BindingResult br,
                               @PathVariable int orderId) {

        orders

    }
}
