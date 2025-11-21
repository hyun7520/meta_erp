package com.meta.stock.order.mapper;

import com.meta.stock.order.dto.DashFlowBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DashMapper {
    DashFlowBean getRequestFlow();
}
