package com.meta.stock.product.mapper;

import com.meta.stock.product.dto.OrderDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<OrderDto> getAllOrders();

}
