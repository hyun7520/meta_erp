package com.meta.stock.order.service;

import com.meta.stock.order.dto.DashFlowBean;
import com.meta.stock.order.mapper.DashMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashService {
    @Autowired
    private DashMapper dashMapper;

    public DashFlowBean getDashFlowBean() {
        return dashMapper.getRequestFlow();
    }
}
