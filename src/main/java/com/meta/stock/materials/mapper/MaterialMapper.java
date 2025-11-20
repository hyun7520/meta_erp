package com.meta.stock.materials.mapper;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.materials.dto.MaterialRequestDto;
import com.meta.stock.materials.dto.MaterialRequirementDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MaterialMapper {

    List<MaterialRequestDto> findAllMaterialRequests();

    public List<MaterialRequestDto> findOngoingMaterialRequests();

    int getCurrentStock(String materialName);

    List<MaterialRequirementDto> calculateRequiredMaterials(Map<String, Object> param);

    List<MaterialDto> getAllMaterials();

    List<MaterialRequestDto> getAllMaterialRequests();
}
