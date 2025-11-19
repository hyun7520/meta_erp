package com.meta.stock.product.Repository;

import com.meta.stock.product.Entity.Production_RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionRequestRepository extends JpaRepository<Production_RequestEntity, Long> {

    // 전체 주문 목록 조회 (최신순)
    List<Production_RequestEntity> findAllByOrderByPrIdDesc();

    // 생산 상태별 조회
    // 대기중: productionStartDate가 null
    List<Production_RequestEntity> findByProductionStartDateIsNullOrderByPrIdDesc();

    // 생산시작: productionStartDate가 있고 endDate가 null
    List<Production_RequestEntity> findByProductionStartDateIsNotNullAndEndDateIsNullOrderByPrIdDesc();

    // 생산완료: endDate가 있음
    List<Production_RequestEntity> findByEndDateIsNotNullOrderByPrIdDesc();
}