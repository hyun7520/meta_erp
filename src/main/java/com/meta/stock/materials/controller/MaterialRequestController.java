package com.meta.stock.materials.controller;

import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.service.MaterialRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pro/material-requests")
public class MaterialRequestController {

    private final MaterialRequestService materialRequestService;

    public MaterialRequestController(MaterialRequestService materialRequestService) {
        this.materialRequestService = materialRequestService;
    }

    // 전체 발주 목록 조회
    @GetMapping
    public ResponseEntity<List<MaterialRequestDto.Response>> getAllMaterialRequests() {
        List<MaterialRequestDto.Response> responses = materialRequestService.getAllMaterialRequests();
        return ResponseEntity.ok(responses);
    }

    // 미승인 발주 목록 조회
    @GetMapping("/pending")
    public ResponseEntity<List<MaterialRequestDto.Response>> getPendingMaterialRequests() {
        List<MaterialRequestDto.Response> responses = materialRequestService.getPendingMaterialRequests();
        return ResponseEntity.ok(responses);
    }

    // 발주 요청 생성
    @PostMapping
    public ResponseEntity<MaterialRequestDto.Response> createMaterialRequest(
            @RequestBody MaterialRequestDto.Request request) {
        MaterialRequestDto.Response response = materialRequestService.createMaterialRequest(request);
        return ResponseEntity.ok(response);
    }

    // 발주 승인 처리
    @PutMapping("/approve")
    public ResponseEntity<MaterialRequestDto.Response> approveMaterialRequest(
            @RequestBody MaterialRequestDto.Approval approval) {
        // approved 필드가 1(승인)인 DTO를 서비스로 전달
        MaterialRequestDto.Approval approveRequest = MaterialRequestDto.Approval.builder()
                .mrId(approval.getMrId())
                .managementEmployee(approval.getManagementEmployee())
                .productionEmployee(approval.getProductionEmployee())
                .approved(1) // 승인
                .note(approval.getNote())
                .build();
        MaterialRequestDto.Response response = materialRequestService.approveMaterialRequest(approveRequest);
        return ResponseEntity.ok(response);
    }

    // 발주 반려 처리
    @PutMapping("/reject")
    public ResponseEntity<MaterialRequestDto.Response> rejectMaterialRequest(
            @RequestBody MaterialRequestDto.Approval approval) {
        // approved 필드가 -1(반려)인 DTO를 서비스로 전달
        MaterialRequestDto.Approval rejectRequest = MaterialRequestDto.Approval.builder()
                .mrId(approval.getMrId())
                .managementEmployee(approval.getManagementEmployee())
                .productionEmployee(approval.getProductionEmployee())
                .approved(-1) // 반려
                .note(approval.getNote())
                .build();
        MaterialRequestDto.Response response = materialRequestService.rejectMaterialRequest(rejectRequest);
        return ResponseEntity.ok(response);
    }
}