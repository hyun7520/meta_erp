package com.meta.stock.materials.service;

import com.meta.stock.materials.dto.*;
import com.meta.stock.materials.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    // 전체 재료 요청 조회
    public Page<MaterialRequestDto> findAllMaterialRequests(String keyword, Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().next().getProperty();
        String sortDir = pageable.getSort().iterator().next().getDirection().name();

        List<MaterialRequestDto> content = materialMapper.findAllMaterialRequestsWithPaging(
                keyword, sortBy, sortDir, offset, limit
        );

        long total = materialMapper.countAllMaterialRequests(keyword);

        return new PageImpl<>(content, pageable, total);
    }

    // 진행상황에 따른 요청 조회
    public Page<MaterialRequestDto> findOngoingMaterialRequests(String keyword, Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().next().getProperty();
        String sortDir = pageable.getSort().iterator().next().getDirection().name();

        List<MaterialRequestDto> content = materialMapper.findMaterialRequestsWithPaging(
                keyword, sortBy, sortDir, offset, limit
        );

        long total = materialMapper.countMaterialRequests(keyword);

        return new PageImpl<>(content, pageable, total);
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
    public MaterialDto getMaterialRequestById(int materialId) {
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

    public List<MaterialCountsBean> getDateMaterialTotals(String serialCode) {
        return materialMapper.getDateMaterialTotals(serialCode);
    }

    // 발주 요청 통계
    public Map<String, Integer> getRequestStatistics() {
        Map<String, Integer> stats = new HashMap<>();

        int pending = materialMapper.countByApproved(0);    // 확인중
        int approved = materialMapper.countByApproved(1);   // 승인
        int completed = materialMapper.countByApproved(2);  // 완료

        stats.put("pending", pending);
        stats.put("approved", approved);
        stats.put("completed", completed);

        return stats;
    }

    // 현재 재료 재고 목록
    public List<MaterialStockDto> getCurrentMaterialStocks() {
        return materialMapper.getCurrentMaterialStocks();
    }
}
