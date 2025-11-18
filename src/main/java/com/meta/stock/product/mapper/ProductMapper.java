package com.meta.stock.product.mapper;

import com.meta.stock.product.dto.ProductDto;
import com.meta.stock.product.dto.ProductStockDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductStockDto> findAllProductStock();

    int getCurrentProductStock(long productId);
}
