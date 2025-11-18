package com.meta.stock.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    // 완료되지 않은 주문
    List<OrderEntity> findByComplete(Integer complete);
}