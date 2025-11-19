package com.meta.stock.materials;

import com.meta.stock.materials.DTO.MaterialRequestApprovalDTO;
import com.meta.stock.materials.DTO.MaterialRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/material-requests")
public class MaterialRequestController {

    private final MaterialRequestService materialRequestService;

    // 생성자 직접 작성
    public MaterialRequestController(MaterialRequestService materialRequestService) {
        this.materialRequestService = materialRequestService;
    }

    // 미승인 발주 건 조회
    @GetMapping("/pending")
    public ResponseEntity<List<MaterialRequestDTO>> getPendingRequests() {
        return ResponseEntity.ok(materialRequestService.getPendingRequests());
    }

    // 모든 발주 건 조회
    @GetMapping
    public ResponseEntity<List<MaterialRequestDTO>> getAllRequests() {
        return ResponseEntity.ok(materialRequestService.getAllRequests());
    }

    // 발주 승인/반려
    @PutMapping("/approve")
    public ResponseEntity<MaterialRequestDTO> approveRequest(
            @RequestBody MaterialRequestApprovalDTO dto) {
        return ResponseEntity.ok(materialRequestService.approveRequest(dto));
    }
}