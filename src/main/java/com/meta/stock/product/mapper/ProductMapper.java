package com.meta.stock.product.mapper;

import com.meta.stock.product.dto.ProductDto;
import com.meta.stock.product.dto.ProductStockDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductStockDto> findTotalProductStock();

    int getCurrentProductStock(long productId);

    List<ProductDto> getProductsByPRId(long productRequestId);

    List<ProductStockDto> findAllProductStockByProduct();

    List<ProductStockDto> findTotalRequiredStock();
}
