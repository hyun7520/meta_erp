package com.meta.stock.materials.Service;

import com.meta.stock.materials.DTO.MaterialRequestDTO;
import com.meta.stock.materials.Entity.MaterialRequestEntity;
import com.meta.stock.materials.Repository.MaterialRequestRepository;
import com.meta.stock.user.Entity.EmployeeEntity;
import com.meta.stock.user.Repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialRequestService {

    private final MaterialRequestRepository materialRequestRepository;
    private final EmployeeRepository employeeRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MaterialRequestService(MaterialRequestRepository materialRequestRepository,
                                  EmployeeRepository employeeRepository) {
        this.materialRequestRepository = materialRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<MaterialRequestDTO.Response> getAllMaterialRequests() {
        List<MaterialRequestEntity> entities = materialRequestRepository.findAllByOrderByMrIdDesc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaterialRequestDTO.Response> getPendingMaterialRequests() {
        List<MaterialRequestEntity> entities = materialRequestRepository.findByApproved(0);
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MaterialRequestDTO.Response createMaterialRequest(MaterialRequestDTO.Request request) {
        MaterialRequestEntity entity = MaterialRequestEntity.builder()
                .requestBy(request.getRequestBy())
                .requestDate(LocalDateTime.now().format(DATE_FORMATTER))
                .qty(request.getQty())
                .unit(request.getUnit())
                .approved(0)
                .note(request.getNote())
                .build();

        MaterialRequestEntity saved = materialRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    @Transactional
    public MaterialRequestDTO.Response approveMaterialRequest(MaterialRequestDTO.Approval approval) {
        return processApproval(approval, 1);
    }

    @Transactional
    public MaterialRequestDTO.Response rejectMaterialRequest(MaterialRequestDTO.Approval approval) {
        return processApproval(approval, -1);
    }

    private MaterialRequestDTO.Response processApproval(MaterialRequestDTO.Approval approval, int status) {
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

    private MaterialRequestDTO.Response convertToResponse(MaterialRequestEntity entity) {
        EmployeeEntity requester = employeeRepository.findById(entity.getRequestBy()).orElse(null);
        EmployeeEntity managementEmp = employeeRepository.findById(entity.getManagementEmployee()).orElse(null);
        EmployeeEntity productionEmp = employeeRepository.findById(entity.getProductionEmployee()).orElse(null);

        String requesterName = requester != null ? requester.getName() : String.valueOf(entity.getRequestBy());
        String managementName = managementEmp != null ? managementEmp.getName() : "-";
        String productionName = productionEmp != null ? productionEmp.getName() : "-";

        return MaterialRequestDTO.Response.builder()
                .mrId(entity.getMrId())
                .materialId(0)
                .materialName("-")
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