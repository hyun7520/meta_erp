package com.meta.stock.materials;

import com.meta.stock.materials.DTO.MaterialRequestApprovalDTO;
import com.meta.stock.materials.DTO.MaterialRequestDTO;
import com.meta.stock.materials.Entity.MaterialRequestEntity;
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
    private static final int MAX_RECORDS = 30;

    public MaterialRequestService(MaterialRequestRepository materialRequestRepository) {
        this.materialRequestRepository = materialRequestRepository;
    }

    // 미승인 발주 건 조회 (approved = 0)
    public List<MaterialRequestDTO> getPendingRequests() {
        deleteOldRecordsIfNeeded();
        return materialRequestRepository.findByApproved(0)
                .stream()
                .sorted((a, b) -> a.getRequestDate().compareTo(b.getRequestDate()))  // 요청일 순 정렬
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 전체 조회 - 미완료 우선, 요청일 순
    public List<MaterialRequestDTO> getAllRequests() {
        deleteOldRecordsIfNeeded();
        return materialRequestRepository.findAllOrderByApprovedAndRequestDate()  //  새 메서드 사용
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
        deleteOldRecordsIfNeeded();
        return convertToDTO(saved);
    }

    @Transactional
    protected void deleteOldRecordsIfNeeded() {
        long count = materialRequestRepository.count();

        if (count > MAX_RECORDS) {
            int deleteCount = (int)(count - MAX_RECORDS);

            // 가장 오래된 레코드 조회 (요청일 기준)
            List<MaterialRequestEntity> oldestRecords = materialRequestRepository
                    .findAll()
                    .stream()
                    .sorted((a, b) -> a.getRequestDate().compareTo(b.getRequestDate()))
                    .limit(deleteCount)
                    .collect(Collectors.toList());

            materialRequestRepository.deleteAll(oldestRecords);

            System.out.println("오래된 원재료 발주 " + deleteCount + "건 삭제됨");
        }
    }


    private MaterialRequestDTO convertToDTO(MaterialRequestEntity entity) {
        MaterialRequestDTO dto = new MaterialRequestDTO();
        dto.setMrId(entity.getMrId());
        dto.setMaterialName(entity.getMaterial() != null ? entity.getMaterial().getMaterialName() : null);
        dto.setRequestByName(entity.getRequestBy() != null ? entity.getRequestBy().getName() : null);
        dto.setRequestDate(entity.getRequestDate());
        dto.setQty(entity.getQty());
        dto.setUnit("box"); // 단위 추가 (DB에 있다면 entity.getUnit()으로 수정)
        dto.setApproved(entity.getApproved());
        dto.setApprovedDate(entity.getApprovedDate());
        dto.setNote(entity.getNote());
        return dto;
    }
}