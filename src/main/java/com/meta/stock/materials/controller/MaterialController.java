package com.meta.stock.materials.controller;

import com.meta.stock.materials.dto.*;
import com.meta.stock.materials.service.MaterialRequestService;
import com.meta.stock.materials.service.MaterialService;
import com.meta.stock.user.employees.dto.EmployeeGetDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class MaterialController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialRequestService materialRequestService;

    // 전체 재료 요청 조회
    @GetMapping("material")
    public String getAllMaterials(Model model, HttpSession session) {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        } else {
            EmployeeGetDto employee = (EmployeeGetDto) session.getAttribute("employee");
            if (employee.getDepartment().contains("경영") || employee.getRole().equals("사원")) {
                return "redirect:/dash";
            }
        }
        // 발주 요청 통계
        Map<String, Integer> requestStats = materialService.getRequestStatistics();

        model.addAttribute("requestStats", requestStats);
        return "product/material";
    }

    // 선택한 제품에 대한 재료 요청서 작성 페이지로 이동
    // AI 추천에 승낙한 경우도 여기서 처리
    @GetMapping("/material/request")
    public String requestMaterials(
            @RequestParam(name = "fmIds", required = false) List<Long> fmIds,
            @RequestParam(name = "materialNames", required = false) List<String> materialNames,
            @RequestParam(name = "quantities", required = false) List<String> quantities,
            @RequestParam(name = "units", required = false) List<String> units,
            Model model, HttpSession session) {

        String requestByName;
        int requestById;
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        } else {
            EmployeeGetDto employee = (EmployeeGetDto) session.getAttribute("employee");
            requestByName = employee.getName();
            requestById = employee.getEmployeeId();
            if (employee.getDepartment().contains("경영") || employee.getRole().equals("사원")) {
                return "redirect:/dash";
            }
        }

        // 필수 파라미터가 없는 상태로 재료 요청 화면으로 넘어가는 경우(URL 강제 입력) 메인 대시보드로 이동
        if (fmIds == null || materialNames == null || quantities == null) {
            return "redirect:/dash";
        }

        // DTO 리스트로 변환
        List<MaterialRequestDto> materialRequests = new ArrayList<>();
        Set<String> checkDupMaterial = new HashSet<>();
        for (int i = 0; i < materialNames.size(); i++) {
            String materialName = materialNames.get(i);
            if(checkDupMaterial.contains(materialName)) {
                MaterialRequestDto materialRequestDto = materialRequests.stream().filter(mr -> mr.getMaterialName().equals(materialName)).findFirst().orElseThrow();
                materialRequestDto.setQty(materialRequestDto.getQty() + Integer.parseInt(quantities.get(i)));
                continue;
            } else{
                checkDupMaterial.add(materialName);
            }
            MaterialRequestDto dto = new MaterialRequestDto();
            dto.setFmId(fmIds.get(i));
            dto.setMaterialName(materialNames.get(i));
            int qty = Math.max(Integer.parseInt(quantities.get(i)), 0);
            dto.setQty(qty);
            dto.setRequestBy(requestById);
            materialRequests.add(dto);
        }

        model.addAttribute("requestByName", requestByName);
        model.addAttribute("materialRequests", materialRequests);

        return "materialRequestForm"; // 발주 입력 페이지
    }

    // 작성한 재료 발주 요청 전송 - DB 반영
    @PostMapping("material/submit")
    public String submitMaterialRequest(
            MaterialRequestFormDto formData,
            RedirectAttributes redirectAttributes) {

        System.out.println("=== 발주 요청 제출 ===");
        System.out.println("요청일: " + formData.getRequestDate());
        System.out.println("\n=== 재료 목록 ===");;
        for (MaterialRequestDto req : formData.getMaterialRequests()) {
            System.out.println(req);
            if(req.getQty() <= 0) {
                continue;
            }
            // TODO: DB 저장 로직 수행
            materialService.save(req);
        }

        // 성공 메시지
        redirectAttributes.addFlashAttribute("message", "발주 요청이 완료되었습니다.");
        redirectAttributes.addFlashAttribute("alertType", "success");

        return "redirect:/product"; // 주문 목록으로 리다이렉트
    }

    @GetMapping("material/update/{mrId}")
    public String getMaterialRequestUpdateForm(
            @PathVariable(name = "mrId") Long mrId,
            Model model, HttpSession session) {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        } else {
            EmployeeGetDto employee = (EmployeeGetDto) session.getAttribute("employee");
            if (employee.getDepartment().contains("경영") || employee.getRole().equals("사원")) {
                return "redirect:/dash";
            }
        }

        // 데이터 확인
        MaterialRequestDto materialRequestDto = materialService.getMaterialRequestById(mrId);

        model.addAttribute("materialRequest", materialRequestDto);
        model.addAttribute("requestBy", materialService.getRequestByName(materialRequestDto.getRequestBy()));

        return "product/materialRequestUpdateForm";
    }

    @PostMapping("material/update-req/{mrId}")
    public String updateMaterialRequests(@PathVariable Long mrId,
                                         @RequestParam Integer qty,
                                         @RequestParam(required = false) String note,
                                         RedirectAttributes redirectAttributes) {

        // 데이터 확인
        materialService.updateMaterialRequest(mrId, qty, note);

        return "redirect:/material";
    }

    @GetMapping("/material/list")
    public ResponseEntity<Map<String, Object>> getMaterialRequestList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "requestDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy));

        // 발주 요청 목록
        Page<MaterialRequestDto> materialRequests =
                materialService.findAllMaterialRequests(keyword, pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("materials", materialRequests);
        result.put("sortBy", sortBy);
        result.put("sortDir", sortDir);
        result.put("keyword", keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/material/progress")
    public ResponseEntity<List<MaterialStockDto>> getMaterialProgress() {
        // 현재 재료 재고
        List<MaterialStockDto> materialStocks = materialService.getCurrentMaterialStocks();
        return ResponseEntity.ok(materialStocks);
    }
}
