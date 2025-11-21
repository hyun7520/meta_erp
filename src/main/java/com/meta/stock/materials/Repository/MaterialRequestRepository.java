package com.meta.stock.materials.Repository;

import com.meta.stock.materials.Entity.MaterialRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRequestRepository extends JpaRepository<MaterialRequestEntity, Long> {

    // 미승인 발주 목록 조회 (approved = 0)
    List<MaterialRequestEntity> findByApproved(int approved);

    // 전체 발주 목록 조회 (최신순)
    List<MaterialRequestEntity> findAllByOrderByMrIdDesc();

    // 특정 요청자의 발주 목록 조회
    List<MaterialRequestEntity> findByRequestBy(long requestBy);
}