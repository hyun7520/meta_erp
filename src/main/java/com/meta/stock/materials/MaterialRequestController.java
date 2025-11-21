package com.meta.stock.materials;

import com.meta.stock.materials.DTO.MaterialRequestDTO;
import com.meta.stock.materials.Service.MaterialRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material-requests")
public class MaterialRequestController {

    private final MaterialRequestService materialRequestService;

    public MaterialRequestController(MaterialRequestService materialRequestService) {
        this.materialRequestService = materialRequestService;
    }

    // 전체 발주 목록 조회
    @GetMapping
    public ResponseEntity<List<MaterialRequestDTO.Response>> getAllMaterialRequests() {
        List<MaterialRequestDTO.Response> responses = materialRequestService.getAllMaterialRequests();
        return ResponseEntity.ok(responses);
    }

    // 미승인 발주 목록 조회
    @GetMapping("/pending")
    public ResponseEntity<List<MaterialRequestDTO.Response>> getPendingMaterialRequests() {
        List<MaterialRequestDTO.Response> responses = materialRequestService.getPendingMaterialRequests();
        return ResponseEntity.ok(responses);
    }

    // 발주 요청 생성
    @PostMapping
    public ResponseEntity<MaterialRequestDTO.Response> createMaterialRequest(
            @RequestBody MaterialRequestDTO.Request request) {
        MaterialRequestDTO.Response response = materialRequestService.createMaterialRequest(request);
        return ResponseEntity.ok(response);
    }

    // 발주 승인 처리
    @PutMapping("/approve")
    public ResponseEntity<MaterialRequestDTO.Response> approveMaterialRequest(
            @RequestBody MaterialRequestDTO.Approval approval) {
        // approved 필드가 1(승인)인 DTO를 서비스로 전달
        MaterialRequestDTO.Approval approveRequest = MaterialRequestDTO.Approval.builder()
                .mrId(approval.getMrId())
                .managementEmployee(approval.getManagementEmployee())
                .productionEmployee(approval.getProductionEmployee())
                .approved(1) // 승인
                .note(approval.getNote())
                .build();
        MaterialRequestDTO.Response response = materialRequestService.approveMaterialRequest(approveRequest);
        return ResponseEntity.ok(response);
    }

    // 발주 반려 처리
    @PutMapping("/reject")
    public ResponseEntity<MaterialRequestDTO.Response> rejectMaterialRequest(
            @RequestBody MaterialRequestDTO.Approval approval) {
        // approved 필드가 -1(반려)인 DTO를 서비스로 전달
        MaterialRequestDTO.Approval rejectRequest = MaterialRequestDTO.Approval.builder()
                .mrId(approval.getMrId())
                .managementEmployee(approval.getManagementEmployee())
                .productionEmployee(approval.getProductionEmployee())
                .approved(-1) // 반려
                .note(approval.getNote())
                .build();
        MaterialRequestDTO.Response response = materialRequestService.rejectMaterialRequest(rejectRequest);
        return ResponseEntity.ok(response);
    }
}