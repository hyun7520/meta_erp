package com.meta.stock.product.repository;

import com.meta.stock.product.entity.ProductionRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionRequestRepository extends JpaRepository<ProductionRequestEntity, Integer> {

    ProductionRequestEntity findProductRequestByPrId(long prId);
}
