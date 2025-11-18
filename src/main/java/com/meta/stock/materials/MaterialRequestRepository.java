package com.meta.stock.materials;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialRequestRepository extends JpaRepository<MaterialRequestEntity, Long> {
    // 미승인 발주 건 조회
    List<MaterialRequestEntity> findByApprovedIsNull();

    // 승인된 발주 건 조회
    List<MaterialRequestEntity> findByApprovedTrue();

    // 반려된 발주 건 조회
    List<MaterialRequestEntity> findByApprovedFalse();
}