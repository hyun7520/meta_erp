package com.meta.stock.materials.service;

import com.meta.stock.config.PythonParser;
import com.meta.stock.lots.mapper.LotsMapper;
import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.entity.MaterialEntity;
import com.meta.stock.materials.entity.MaterialRequestEntity;
import com.meta.stock.materials.entity.FixedMaterialEntity;
import com.meta.stock.materials.mapper.MaterialMapper;
import com.meta.stock.materials.repository.MaterialRequestRepository;
import com.meta.stock.materials.repository.FixedMaterialRepository;
import com.meta.stock.user.employees.entity.EmployeeEntity;
import com.meta.stock.user.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service("materialRequestServiceV2")  // 빈 이름 지정
public class MaterialRequestServiceV2 {

    @Autowired
    private final MaterialRequestRepository materialRequestRepository;
    @Autowired
    private final FixedMaterialRepository fixedMaterialRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final LotsMapper lotsMapper;
    @Autowired
    private final MaterialMapper materialMapper;
    @Autowired
    private PythonParser parser;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MaterialRequestServiceV2(
            MaterialRequestRepository materialRequestRepository,
            FixedMaterialRepository fixedMaterialRepository,
            EmployeeRepository employeeRepository,
            LotsMapper lotsMapper, MaterialMapper materialMapper) {
        this.materialRequestRepository = materialRequestRepository;
        this.fixedMaterialRepository = fixedMaterialRepository;
        this.employeeRepository = employeeRepository;
        this.lotsMapper = lotsMapper;
        this.materialMapper = materialMapper;
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

        // NOTE에 fm_id 저장 (형식: "fm_id:실제비고")
        String noteWithFmId = request.getFmId() + ":";
        if (request.getNote() != null && !request.getNote().isEmpty()) {
            noteWithFmId += request.getNote();
        }
        entity.setNote(noteWithFmId);

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

        if (status == 1) {
            // 기존 NOTE에서 fm_id 부분 유지
            String existingNote = entity.getNote();
            String fmIdPart = "";
            if (existingNote != null && existingNote.contains(":")) {
                fmIdPart = existingNote.split(":", 2)[0] + ":";
            }
            entity.setNote(fmIdPart + approval.getNote());
            saveMaterial(entity);
        } else if (status == -1) {
            String existingNote = entity.getNote();
            String fmIdPart = "";
            if (existingNote != null && existingNote.contains(":")) {
                fmIdPart = existingNote.split(":", 2)[0] + ":";
            }
            entity.setNote(fmIdPart + "반려됨");
        }

        MaterialRequestEntity saved = materialRequestRepository.save(entity);
        return convertToResponse(saved);
    }

    private MaterialRequestDto.Response convertToResponse(MaterialRequestEntity entity) {
        EmployeeEntity requester = entity.getRequestBy() != null
                ? employeeRepository.findById(entity.getRequestBy().intValue()).orElse(null)
                : null;
        EmployeeEntity managementEmp = entity.getManagementEmployee() != null && entity.getManagementEmployee() > 0
                ? employeeRepository.findById(entity.getManagementEmployee().intValue()).orElse(null)
                : null;
        EmployeeEntity productionEmp = entity.getProductionEmployee() != null && entity.getProductionEmployee() > 0
                ? employeeRepository.findById(entity.getProductionEmployee().intValue()).orElse(null)
                : null;

        String requesterName = requester != null ? requester.getName() : String.valueOf(entity.getRequestBy());
        String managementName = managementEmp != null ? managementEmp.getName() : "-";
        String productionName = productionEmp != null ? productionEmp.getName() : "-";

        // NOTE에서 fm_id 추출
        long fmId = entity.getFmId();
        String materialName = "-";
        String actualNote = "";

        try {
            if (entity.getNote() != null && entity.getNote().contains(":")) {
                String[] parts = entity.getNote().split(":", 2);
                actualNote = parts.length > 1 ? parts[1] : "";
            } else {
                actualNote = entity.getNote();
            }

            // fm_id로 Fixed_material 조회
            FixedMaterialEntity fixedMaterial = fixedMaterialRepository.findById(fmId).orElse(null);
            if (fixedMaterial != null) {
                materialName = fixedMaterial.getName();
            }
        } catch (NumberFormatException e) {
            // fm_id가 숫자가 아니면 전체를 비고로 처리
            actualNote = entity.getNote();
        }

        return MaterialRequestDto.Response.builder()
                .mrId(entity.getMrId())
                .materialId(fmId)
                .materialName(materialName)
                .requestBy(entity.getRequestBy())
                .requesterName(requesterName)
                .requestDate(entity.getRequestDate())
                .qty(entity.getQty())
                .unit(entity.getUnit())
                .approved(entity.getApproved())
                .approvedDate(entity.getApprovedDate())
                .managementEmployeeName(managementName)
                .productionEmployeeName(productionName)
                .note(actualNote)
                .build();
    }

    private void saveMaterial(MaterialRequestEntity request) {
        FixedMaterialEntity fixedMaterial = fixedMaterialRepository.findById(request.getFmId()).orElseThrow();
        int lossQty = parser.getLossPerProductAndYearMonth(fixedMaterial.getName(), request.getQty());
        int createdQty = (request.getQty() - lossQty) <= 0 ? 1 : (request.getQty() - lossQty);
        lotsMapper.storeProduct(createdQty, fixedMaterial.getLifeTime());
        Long lotsId = lotsMapper.getLatestLot();

        MaterialEntity material = new MaterialEntity();
        material.setLotsId(lotsId);
        material.setMaterialName(fixedMaterial.getName());

        material.setMaterialLoss(lossQty); // 원자재 로스율(가져와 insert)
        material.setMrId(request.getMrId());
        materialMapper.materialSave(material);
    }
}