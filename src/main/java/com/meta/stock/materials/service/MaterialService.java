package com.meta.stock.materials.service;

import com.meta.stock.materials.dto.MaterialCountsBean;
import com.meta.stock.materials.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialService {
    @Autowired
    private MaterialMapper materialMapper;

    public List<MaterialCountsBean> getDateMaterialTotals(String serialCode) {
        return materialMapper.getDateMaterialTotals(serialCode);
    }
}
