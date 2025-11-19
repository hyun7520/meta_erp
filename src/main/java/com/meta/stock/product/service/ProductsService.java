package com.meta.stock.product.service;

import com.meta.stock.product.mapper.ProductMapper;
import com.meta.stock.product.dto.ProductsAmountListBean;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductsService {
    @Autowired
    private ProductMapper productMapper;

    private ModelMapper modelMapper = new ModelMapper();

    public List<ProductsAmountListBean> getDashTable(Map<String, Object> param) {
        return productMapper.getDashProductsList(param);
    }

    public int getDashTableTotal(Map<String, Object> param) {
        return productMapper.getTotalListSize(param);
    }
}
