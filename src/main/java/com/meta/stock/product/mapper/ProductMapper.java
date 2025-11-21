package com.meta.stock.product.mapper;

import com.meta.stock.product.dto.ProductDemandBean;
import com.meta.stock.product.dto.ProductsAmountListBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    List<ProductsAmountListBean> getDashProductsList(Map<String, Object> param);
    List<ProductDemandBean> getDashProductDemand();

    int getTotalListSize(Map<String, Object> param);
}
