package com.meta.stock.product.mapper;

import com.meta.stock.product.dto.ProductRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductionRequestMapper {

    List<ProductRequestDto> findOngoingProductRequests();

    List<ProductRequestDto> findAllProductionRequests();

    ProductRequestDto findProductRequestById(long orderId);

    void updateOrderStatus(Long orderId, int status);
}
