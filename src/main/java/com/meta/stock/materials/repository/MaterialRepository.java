package com.meta.stock.materials.repository;

import com.meta.stock.materials.entity.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<MaterialEntity, Long> {

    // 원재료명으로 조회
    Optional<MaterialEntity> findByMaterialName(String materialName);

    // 전체 원재료 목록 조회 (ID 순)
    List<MaterialEntity> findAllByOrderByMaterialsIdAsc();
}