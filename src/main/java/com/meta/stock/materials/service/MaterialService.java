package com.meta.stock.materials.service;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.materials.dto.MaterialRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    // 재료 요청 조회
    public List<MaterialRequestDto> getMaterialRequests() {
        return null;
    }
    
    // 세부 재료 요청 조회
    public List<MaterialDto> getMaterialRequestDetails(int mrId) {
        return null;
    }

    // 전체 재료 조회
    public List<MaterialDto> getMaterials(List<Integer> materials) {
        return null;
    }

    // 재료 요청
    public String addRequest() {
        return null;
    }

    // 상세 재료 정보 조회
    public MaterialDto getMaterialById(int materialId) {
        return null;
    }
}
