package com.meta.stock.materials.mapper;

import com.meta.stock.materials.dto.*;
import com.meta.stock.materials.entity.MaterialEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MaterialMapper {

    List<MaterialRequestDto> findMaterialRequestsWithPaging(String keyword, String sortBy, String sortDir, int offset, int limit);

    long countMaterialRequests(String keyword);

    List<MaterialRequestDto> findAllMaterialRequestsWithPaging(String keyword, String sortBy, String sortDir, int offset, int limit);

    long countAllMaterialRequests(String keyword);

    int getCurrentStock(String materialName);

    List<MaterialRequirementDto> calculateRequiredMaterials(Long fpId, int qty);

    List<MaterialCountsBean> getDateMaterialTotals(String serialCode);

    int countByApproved(int i);

    List<MaterialStockDto> getCurrentMaterialStocks();

    String getRequestByName(Long id);

    void save(MaterialRequestDto req); // 원자재 '요청'용

    void materialSave(MaterialEntity entity); // 원자재 '저장'용

    MaterialRequestDto getMaterialRequestById(Long mrId);

    void updateMaterialRequest(Long mrId, Integer qty, String note);

    List<MaterialDto> getRequiredMaterials(Long fpId);

    void decreaseMaterialStock(Long fmId, int qty);
}
