package com.meta.stock.product.repository;

import com.meta.stock.product.entity.ProductionRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionRequestRepository extends JpaRepository<ProductionRequestEntity, Long> {

    ProductionRequestEntity findProductRequestByPrId(long prId);

    // 전체 주문 목록 조회 (최신순)
    List<ProductionRequestEntity> findAllByOrderByPrIdDesc();

    // 생산 상태별 조회
    // 대기중: productionStartDate가 null
    List<ProductionRequestEntity> findByProductionStartDateIsNullOrderByPrIdDesc();

    // 생산시작: productionStartDate가 있고 endDate가 null
    List<ProductionRequestEntity> findByProductionStartDateIsNotNullAndEndDateIsNullOrderByPrIdDesc();

    // 생산완료: endDate가 있음
    List<ProductionRequestEntity> findByEndDateIsNotNullOrderByPrIdDesc();
}
