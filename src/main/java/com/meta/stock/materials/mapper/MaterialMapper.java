package com.meta.stock.materials.mapper;

import com.meta.stock.materials.dto.*;
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

    List<MaterialRequirementDto> calculateRequiredMaterials(Map<String, Object> param);

    List<MaterialCountsBean> getDateMaterialTotals(String serialCode);

    int countByApproved(int i);

    List<MaterialStockDto> getCurrentMaterialStocks();
}
