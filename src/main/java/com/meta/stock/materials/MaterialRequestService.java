package com.meta.stock.materials;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MaterialRequestService {

    private final MaterialRequestRepository materialRequestRepository;

    public MaterialRequestService(MaterialRequestRepository materialRequestRepository) {
        this.materialRequestRepository = materialRequestRepository;
    }

    // 미승인 발주 건 조회 (approved = 0)
    public List<MaterialRequestDTO> getPendingRequests() {
        return materialRequestRepository.findByApproved(0)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MaterialRequestDTO> getAllRequests() {
        return materialRequestRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MaterialRequestDTO approveRequest(MaterialRequestApprovalDTO dto) {
        MaterialRequestEntity request = materialRequestRepository.findById(dto.getMrId())
                .orElseThrow(() -> new RuntimeException("발주 요청을 찾을 수 없습니다."));

        request.setApproved(dto.getApproved());

        LocalDate now = LocalDate.now();
        String approvedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        request.setApprovedDate(approvedDate);

        if (dto.getNote() != null) {
            request.setNote(dto.getNote());
        }

        MaterialRequestEntity saved = materialRequestRepository.save(request);
        return convertToDTO(saved);
    }

    private MaterialRequestDTO convertToDTO(MaterialRequestEntity entity) {
        MaterialRequestDTO dto = new MaterialRequestDTO();
        dto.setMrId(entity.getMrId());
        dto.setMaterialName(entity.getMaterial() != null ? entity.getMaterial().getMaterialName() : null);
        dto.setRequestByName(entity.getRequestBy() != null ? entity.getRequestBy().getName() : null);
        dto.setRequestDate(entity.getRequestDate());
        dto.setQty(entity.getQty());
        dto.setUnit("kg"); // 단위 추가 (DB에 있다면 entity.getUnit()으로 수정)
        dto.setApproved(entity.getApproved());
        dto.setApprovedDate(entity.getApprovedDate());
        dto.setNote(entity.getNote());
        return dto;
    }
}