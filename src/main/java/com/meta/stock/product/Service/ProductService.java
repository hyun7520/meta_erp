package com.meta.stock.product.Service;

import com.meta.stock.product.DTO.ProductDTO;
import com.meta.stock.product.Entity.ProductsEntity;
import com.meta.stock.product.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // 생성자
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 전체 제품 목록 조회
    @Transactional(readOnly = true)
    public List<ProductDTO.Response> getAllProducts() {
        List<ProductsEntity> entities = productRepository.findAllByOrderByProductIdAsc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Entity -> Response DTO 변환
    private ProductDTO.Response convertToResponse(ProductsEntity entity) {
        return ProductDTO.Response.builder()
                .productId(entity.getProductId() != null ? entity.getProductId() : 0)
                .productName(entity.getProductName())
                .productionLoss(entity.getProductionLoss() != null ? entity.getProductionLoss() : 0)
                .prId(entity.getPrId() != null ? entity.getPrId() : 0)
                .lotsId(entity.getLotsId() != null ? entity.getLotsId() : 0)
                .build();
    }
}