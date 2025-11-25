package com.meta.stock.materials.controller;

import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.service.MaterialRequestServiceV2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pro/v2/material-requests")  // v2 경로
public class MaterialRequestControllerV2 {

    private final MaterialRequestServiceV2 materialRequestService;

    public MaterialRequestControllerV2(@Qualifier("materialRequestServiceV2") MaterialRequestServiceV2 materialRequestService) {
        this.materialRequestService = materialRequestService;
    }

    @GetMapping
    public ResponseEntity<List<MaterialRequestDto.Response>> getAllMaterialRequests() {
        List<MaterialRequestDto.Response> responses = materialRequestService.getAllMaterialRequests();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<MaterialRequestDto.Response>> getPendingMaterialRequests() {
        List<MaterialRequestDto.Response> responses = materialRequestService.getPendingMaterialRequests();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<MaterialRequestDto.Response> createMaterialRequest(
            @RequestBody MaterialRequestDto.Request request) {
        MaterialRequestDto.Response response = materialRequestService.createMaterialRequest(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/approve")
    public ResponseEntity<MaterialRequestDto.Response> approveMaterialRequest(
            @RequestBody MaterialRequestDto.Approval approval) {
        MaterialRequestDto.Approval approveRequest = MaterialRequestDto.Approval.builder()
                .mrId(approval.getMrId())
                .managementEmployee(approval.getManagementEmployee())
                .productionEmployee(approval.getProductionEmployee())
                .approved(1)
                .note(approval.getNote())
                .build();
        MaterialRequestDto.Response response = materialRequestService.approveMaterialRequest(approveRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reject")
    public ResponseEntity<MaterialRequestDto.Response> rejectMaterialRequest(
            @RequestBody MaterialRequestDto.Approval approval) {
        MaterialRequestDto.Approval rejectRequest = MaterialRequestDto.Approval.builder()
                .mrId(approval.getMrId())
                .managementEmployee(approval.getManagementEmployee())
                .productionEmployee(approval.getProductionEmployee())
                .approved(-1)
                .note(approval.getNote())
                .build();
        MaterialRequestDto.Response response = materialRequestService.rejectMaterialRequest(rejectRequest);
        return ResponseEntity.ok(response);
    }
}