package com.meta.stock.materials.service;

import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.entity.MaterialRequestEntity;
import com.meta.stock.materials.entity.MaterialEntity;
import com.meta.stock.materials.repository.MaterialRequestRepository;
import com.meta.stock.materials.repository.MaterialRepository;
import com.meta.stock.user.employees.entity.EmployeeEntity;
import com.meta.stock.user.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialRequestService {

    @Autowired
    private final MaterialRequestRepository materialRequestRepository;
    @Autowired
    private final MaterialRepository materialRepository;  // 추가
    @Autowired
    private final EmployeeRepository employeeRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MaterialRequestService(MaterialRequestRepository materialRequestRepository,
                                  MaterialRepository materialRepository,  // 추가
                                  EmployeeRepository employeeRepository) {
        this.materialRequestRepository = materialRequestRepository;
        this.materialRepository = materialRepository;  // 추가
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<MaterialRequestDto.Response> getAllMaterialRequests() {
        List<MaterialRequestEntity> entities = materialRequestRepository.findAllByOrderByMrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaterialRequestDto.Response> getPendingMaterialRequests() {
        List<MaterialRequestEntity> entities = materialRequestRepository.findByApproved(0);
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MaterialRequestDto.Response createMaterialRequest(MaterialRequestDto.Request request) {
        MaterialRequestEntity entity = new MaterialRequestEntity();
        entity.setRequestBy(request.getRequestBy());
        entity.setRequestDate(LocalDateTime.now().format(DATETIME_FORMATTER));
        entity.setQty(request.getQty());
        entity.setUnit(request.getUnit());
        entity.setApproved(0);
        entity.setNote(request.getNote());

        MaterialRequestEntity saved = materialRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    @Transactional
    public MaterialRequestDto.Response approveMaterialRequest(MaterialRequestDto.Approval approval) {
        return processApproval(approval, 1);
    }

    @Transactional
    public MaterialRequestDto.Response rejectMaterialRequest(MaterialRequestDto.Approval approval) {
        return processApproval(approval, -1);
    }

    private MaterialRequestDto.Response processApproval(MaterialRequestDto.Approval approval, int status) {
        MaterialRequestEntity entity = materialRequestRepository.findById(approval.getMrId())
                .orElseThrow(() -> new IllegalArgumentException("발주 요청을 찾을 수 없습니다."));

        entity.setApproved(status);
        entity.setManagementEmployee(approval.getManagementEmployee());
        entity.setProductionEmployee(approval.getProductionEmployee());
        entity.setApprovedDate(LocalDateTime.now().format(DATE_FORMATTER));

        if (approval.getNote() != null && !approval.getNote().isEmpty()) {
            entity.setNote(approval.getNote());
        } else if (status == -1) {
            entity.setNote("반려됨");
        }

        MaterialRequestEntity saved = materialRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    // 이 메서드만 수정
    private MaterialRequestDto.Response convertToResponse(MaterialRequestEntity entity) {
        EmployeeEntity requester = employeeRepository.findById(entity.getRequestBy().intValue()).orElse(null);
        EmployeeEntity managementEmp = entity.getManagementEmployee() > 0
                ? employeeRepository.findById(entity.getManagementEmployee().intValue()).orElse(null)
                : null;
        EmployeeEntity productionEmp = entity.getProductionEmployee() > 0
                ? employeeRepository.findById(entity.getProductionEmployee().intValue()).orElse(null)
                : null;

        String requesterName = requester != null ? requester.getName() : String.valueOf(entity.getRequestBy());
        String managementName = managementEmp != null ? managementEmp.getName() : "-";
        String productionName = productionEmp != null ? productionEmp.getName() : "-";

        // 원재료 정보 가져오기 (추가된 부분)
        List<MaterialEntity> materials = materialRepository.findByMrId(entity.getMrId());

        String materialName = "-";
        long materialId = 0;

        if (!materials.isEmpty()) {
            MaterialEntity material = materials.get(0);
            materialName = material.getMaterialName() != null ? material.getMaterialName() : "-";
            materialId = material.getMaterialsId();
        }

        return MaterialRequestDto.Response.builder()
                .mrId(entity.getMrId())
                .materialId(materialId)
                .materialName(materialName)  // 실제 원재료명이 들어감
                .requestBy(entity.getRequestBy())
                .requesterName(requesterName)
                .requestDate(entity.getRequestDate())
                .qty(entity.getQty())
                .unit(entity.getUnit())
                .approved(entity.getApproved())
                .approvedDate(entity.getApprovedDate())
                .managementEmployeeName(managementName)
                .productionEmployeeName(productionName)
                .note(entity.getNote())
                .build();
    }
}