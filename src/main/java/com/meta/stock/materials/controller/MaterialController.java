package com.meta.stock.materials.controller;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.materials.dto.MaterialStockDto;
import com.meta.stock.materials.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    // 전체 재료 요청 조회
    @GetMapping("material")
    public String getAllMaterials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "requestDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            Model model) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy));

        // 발주 요청 목록
        Page<MaterialRequestDto> materialRequests =
                materialService.findAllMaterialRequests(keyword, pageable);

        // 발주 요청 통계
        Map<String, Integer> requestStats = materialService.getRequestStatistics();

        // 현재 재료 재고
        List<MaterialStockDto> materialStocks = materialService.getCurrentMaterialStocks();

        model.addAttribute("materialRequests", materialRequests);
        model.addAttribute("requestStats", requestStats);
        model.addAttribute("materialStocks", materialStocks);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);

        return "material";
    }

    // 선택한 제품에 대한 재료 요청서 작성 페이지로 이동
    // AI 추천에 승낙한 경우도 여기서 처리
    @GetMapping("/material/request")
    public String requestMaterials(
            @RequestParam(name = "materialNames") List<String> materialNames,
            @RequestParam(name = "quantities") List<String> quantities,
            @RequestParam(name = "units") List<String> units,
            Model model) {

        // 받은 데이터 확인 (로그)
        System.out.println("=== 선택된 재료 목록 ===");
        for (int i = 0; i < materialNames.size(); i++) {
            System.out.printf("재료명: %s, 수량: %s %s%n",
                    materialNames.get(i),
                    quantities.get(i),
                    units.get(i));
        }

        // DTO 리스트로 변환
        List<MaterialRequestDto> materialRequests = new ArrayList<>();
        for (int i = 0; i < materialNames.size(); i++) {
            MaterialRequestDto dto = new MaterialRequestDto();
            dto.setMaterialName(materialNames.get(i));
            dto.setQuantity(Double.parseDouble(quantities.get(i)));
            dto.setUnit(units.get(i));
            materialRequests.add(dto);
        }

        // 모델에 담아서 뷰로 전달
        model.addAttribute("materialRequests", materialRequests);

        return "material/requestForm"; // 발주 입력 페이지
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
