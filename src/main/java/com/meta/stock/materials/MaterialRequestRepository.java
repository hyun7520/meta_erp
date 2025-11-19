package com.meta.stock.materials;

import com.meta.stock.materials.Entity.MaterialRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialRequestRepository extends JpaRepository<MaterialRequestEntity, Long> {
    // 미승인 발주 건 조회 (approved = 0)
    List<MaterialRequestEntity> findByApproved(int approved);

    // 전체 조회 - 미완료 우선, 요청일 순 정렬
    @Query("SELECT m FROM MaterialRequestEntity m ORDER BY " +
            "CASE WHEN m.approved = 0 THEN 0 ELSE 1 END, " +
            "m.requestDate ASC")
    List<MaterialRequestEntity> findAllOrderByApprovedAndRequestDate();
}