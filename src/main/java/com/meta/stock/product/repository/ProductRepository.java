package com.meta.stock.product.repository;

import com.meta.stock.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByOrderByProductIdAsc();

    //  제품 ID로 시리얼코드만 조회
    @Query("SELECT p.serialCode FROM FixedProductEntity p WHERE p.fpId = :productId")
    Optional<String> findSerialCodeById(@Param("productId") Long productId);
}