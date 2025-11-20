package com.meta.stock.materials.service;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.materials.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    // 전체 재료 조회
    public List<MaterialDto> getAllMaterials() {
        return materialMapper.getAllMaterials();
    }

    // 전체 재료 요청 조회
    public List<MaterialRequestDto> findAllMaterialRequests() {
        return materialMapper.findAllMaterialRequests();
    }

    // 진행상황에 따른 요청 조회
    public List<MaterialRequestDto> findOngoingMaterialRequests() {
        return materialMapper.findOngoingMaterialRequests();
    }
    
    // 세부 재료 요청 조회
    public List<MaterialDto> getMaterialRequestDetails(int mrId) {
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

    // 현재 남은 재고의 개수를 반환한다.
    public List<MaterialRequirementDto> calculateRequiredMaterials(long productId, int qty) {
        Map<String, Object> param = new HashMap<>();
        param.put("productId", productId);
        param.put("qty", qty);
        return materialMapper.calculateRequiredMaterials(param);
    }

    public int getCurrentStock(String materialName) {
        return materialMapper.getCurrentStock(materialName);
    }
    
}
