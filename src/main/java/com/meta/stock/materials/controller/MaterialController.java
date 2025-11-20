package com.meta.stock.materials.controller;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.materials.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    // 전체 재료 요청 조회
    @GetMapping("material")
    public String getAllMaterials(Model model) {

        List<MaterialRequestDto> mrDto = materialService.findAllMaterialRequests();

        model.addAttribute("mrDto", mrDto);

        return "material";
    }

    // 특정 재료 상세 정보 조회
    @GetMapping("material/{materialId}")
    public String getMaterialById(@PathVariable int mrId) {

        MaterialDto foundMaterial = materialService.getMaterialRequestById(mrId);

        return null;
    }

    // 이전에 요청한 재료 요청서 확인
    @GetMapping("material/order/{mrId}")
    public List<MaterialDto> getMaterialRequestDetails(@PathVariable int mrId) {
        return materialService.getMaterialRequestDetails(mrId);
    }

    // 선택한 제품에 대한 재료 요청서 작성 페이지로 이동
    // AI 추천에 승낙한 경우도 여기서 처리
    @GetMapping("material/order-form/{productId}")
    public String getMaterialRequestForm(@PathVariable(required = false) int productId,
                                         @RequestParam List<Integer> materials,
                                         Model model) {

        List<MaterialDto> materialList =  materialService.getAllMaterials();
        model.addAttribute(materialList);

        return "material_request";
    }

    // 작성한 재료 발주 요청 전송 - DB 반영
    @PostMapping("material/order-form/{productId}")
    public String sendMaterialRequest(@Valid @ModelAttribute("materialDto") MaterialDto dto,
                                      BindingResult br,
                                      @RequestParam int productId) {

        if(br.hasErrors()){
            // return form
        }
        // DB 반영 성공 여부 return
        return materialService.addRequest();
    }
}
