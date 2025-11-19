package com.meta.stock.order.repository;

import com.meta.stock.order.entity.LotsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotsRepository extends JpaRepository<LotsEntity, Long> {
}
