package com.meta.stock.product.service;

import com.meta.stock.product.mapper.ProductMapper;
import com.meta.stock.product.dto.ProductsAmountListBean;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductsService {
    @Autowired
    private ProductMapper productMapper;

    private ModelMapper modelMapper = new ModelMapper();

    public Page<ProductsAmountListBean> getList(Map<String, Object> param) {
        List<ProductsAmountListBean> list = productMapper.getDashProductsList(param);
        int totalSize = productMapper.getTotalListSize(param);

        Pageable pageable = PageRequest.of((int) param.get("page"), (int) param.get("limit"));

        return new PageImpl<>(list, pageable, totalSize);
    }
}
