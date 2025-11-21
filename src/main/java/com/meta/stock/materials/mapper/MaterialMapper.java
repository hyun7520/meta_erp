package com.meta.stock.materials.mapper;

import com.meta.stock.materials.dto.MaterialCountsBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MaterialMapper {
    List<MaterialCountsBean> getDateMaterialTotals(String serialCode);
}
