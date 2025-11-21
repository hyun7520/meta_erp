package com.meta.stock.product.service;

import com.meta.stock.product.entity.FixedProductEntity;
import com.meta.stock.product.mapper.ProductMapper;
import com.meta.stock.product.dto.ProductsAmountListBean;
import com.meta.stock.product.repository.FixedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductsService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private FixedProductRepository fixedProductRepository;

    public List<ProductsAmountListBean> getDashTable(Map<String, Object> param) {
        return productMapper.getDashProductsList(param);
    }

    public int getDashTableTotal(Map<String, Object> param) {
        return productMapper.getTotalListSize(param);
    }


    public Map<String, String> getFixedProducts() {
        Map<String, String> map = new HashMap<>();
        List<FixedProductEntity> list = fixedProductRepository.findAll();
        list.forEach(entity -> {
            map.put(entity.getName(), entity.getSerialCode());
        });

        return map;
    }
}
