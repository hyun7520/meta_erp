package com.meta.stock.lots.repository;

import com.meta.stock.lots.entity.LotsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotsRepository extends JpaRepository<LotsEntity, Long> {
}
