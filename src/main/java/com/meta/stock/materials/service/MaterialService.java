package com.meta.stock.materials.service;

import com.meta.stock.lots.dto.LotStockDto;
import com.meta.stock.lots.mapper.LotsMapper;
import com.meta.stock.materials.dto.*;
import com.meta.stock.materials.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MaterialService {

    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private LotsMapper lotsMapper;

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
    public Page<MaterialRequestDto> findMaterialRequestsWithPaging(String keyword, Pageable pageable) {
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

    // 상세 재료 정보 조회
    public MaterialRequestDto getMaterialRequestById(Long mrId) {
        return materialMapper.getMaterialRequestById(mrId);
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

    public String getRequestByName(Long id) {
        return materialMapper.getRequestByName(id);
    }

    public void save(MaterialRequestDto req) {
        materialMapper.save(req);
    }

    public void updateMaterialRequest(Long mrId, Integer qty, String note) {
        materialMapper.updateMaterialRequest(mrId, qty, note);
    }

    public void decreaseMaterial(Long fpId, Integer qty) {
        List<MaterialRequirementDto> mDto = materialMapper.calculateRequiredMaterials(fpId, qty);
        for(MaterialRequirementDto dto : mDto) {
            System.out.println(dto.getFmId());
            System.out.println(dto.getMaterialName());
            System.out.println(dto.getRequiredQty());
            System.out.println("-----------------");
            List<LotStockDto> lots = lotsMapper.findLotsByFmIdByExpiry(dto.getFmId());
            int remainingQty = dto.getRequiredQty() * qty;
            for (LotStockDto lot : lots) {
                if (remainingQty <= 0) break;

                int currentQty = lot.getQty();
                if (currentQty <= 0) continue;

                // 이번 로트에서 출하할 수량
                int toShip = Math.min(currentQty, remainingQty);
                int newQty = currentQty - toShip;
                remainingQty -= toShip;

                System.out.println("currentQty:"+currentQty);
                System.out.println("remainingQty:"+remainingQty);

                // 5. 로트 재고 업데이트
                lotsMapper.updateLotQty(lot.getLotId(), newQty);
            }
        }
    }
}
