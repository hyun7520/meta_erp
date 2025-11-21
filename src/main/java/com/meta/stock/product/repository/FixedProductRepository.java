package com.meta.stock.product.repository;

import com.meta.stock.product.entity.FixedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixedProductRepository extends JpaRepository<FixedProductEntity, Integer> {
}
