package com.meta.stock.materials.repository;

import com.meta.stock.materials.entity.FixedMaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FixedMaterialRepository extends JpaRepository<FixedMaterialEntity, Long> {

    // fm_id로 조회
    Optional<FixedMaterialEntity> findByFmId(Long fmId);

    // 전체 목록
    List<FixedMaterialEntity> findAllByOrderByFmIdAsc();
}