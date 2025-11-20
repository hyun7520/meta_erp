package com.meta.stock.product.service;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.product.dto.ProductDto;
import com.meta.stock.product.dto.ProductStockDto;
import com.meta.stock.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public List<ProductStockDto> findTotalProductStock() {
        return productMapper.findTotalProductStock();
    }
    public List<ProductDto> getProductsByPRId(long orderId) {
        return productMapper.getProductsByPRId(orderId);
    }
    public int getCurrentProductStock(long productId) {
        return productMapper.getCurrentProductStock(productId);
    }

    public List<ProductStockDto> findAllProductStockByProduct() {
        return productMapper.findAllProductStockByProduct();
    }

    public List<ProductStockDto> findTotalRequiredStock() {
        return productMapper.findTotalRequiredStock();
    }

}
