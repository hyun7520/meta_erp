package com.meta.stock.product.service;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.product.dto.ProductDto;
import com.meta.stock.product.mapper.OrderMapper;
import com.meta.stock.product.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class ProductService {

    public List<ProductDto> getAllProducts() {
        return null;
    }

    public List<ProductDto> getSelectedProducts(List<Integer> items) {
        return null;
    }

    public List<MaterialDto> getMaterialStatus(@PathVariable int productId) {
        return null;
    }
}
