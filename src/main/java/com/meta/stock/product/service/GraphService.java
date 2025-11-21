package com.meta.stock.product.service;

import com.meta.stock.product.dto.ProductDemandBean;
import com.meta.stock.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class GraphService {
    // 해당 파일은 Graph들을 그리기 위한 Class 파일
    @Autowired
    private ProductMapper productMapper;

    public List<ProductDemandBean> getList() {
        return productMapper.getDashProductDemand();
    }
}
