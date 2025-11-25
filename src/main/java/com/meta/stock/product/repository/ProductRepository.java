package com.meta.stock.product.repository;

import com.meta.stock.product.dto.ProductListDTO;
import com.meta.stock.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByOrderByProductIdAsc();

    //  제품 목록 조회 (필요한 필드만)
    @Query("SELECT new com.meta.stock.product.dto.ProductListDTO(p.productId, p.productName) " +
            "FROM ProductEntity p ORDER BY p.productId ASC")
    List<ProductListDTO> findAllProductsForOrder();

    //  제품 ID로 제품명만 조회
    @Query("SELECT p.productName FROM ProductEntity p WHERE p.productId = :productId")
    Optional<String> findProductNameById(@Param("productId") Long productId);
}