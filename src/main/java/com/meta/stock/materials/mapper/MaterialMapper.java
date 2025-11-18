package com.meta.stock.materials.mapper;

import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.dto.MaterialRequirementDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MaterialMapper {

    public List<MaterialRequestDto> findAllRequests(int keyword);

    int getCurrentStock(String materialName);

    List<MaterialRequirementDto> calculateRequiredMaterials(Map<String, Object> param);
}
