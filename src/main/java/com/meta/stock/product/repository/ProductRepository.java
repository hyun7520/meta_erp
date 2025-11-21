package com.meta.stock.product.repository;

import com.meta.stock.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByProductId(long productId);

    // 제품명으로 조회
    Optional<ProductEntity> findByProductName(String productName);

    // 전체 제품 목록 조회 (ID 순)
    List<ProductEntity> findAllByOrderByProductIdAsc();
}
