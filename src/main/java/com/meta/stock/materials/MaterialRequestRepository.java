package com.meta.stock.materials;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialRequestRepository extends JpaRepository<MaterialRequestEntity, Long> {
    // 미승인 발주 건 조회 (approved = 0)
    List<MaterialRequestEntity> findByApproved(int approved);

    // 또는 전체 조회 후 Service에서 필터링
}