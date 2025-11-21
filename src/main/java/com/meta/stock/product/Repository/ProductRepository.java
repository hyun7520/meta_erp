package com.meta.stock.product.Repository;

import com.meta.stock.product.Entity.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductsEntity, Long> {

    // 제품명으로 조회
    Optional<ProductsEntity> findByProductName(String productName);

    // 전체 제품 목록 조회 (ID 순)
    List<ProductsEntity> findAllByOrderByProductIdAsc();
}