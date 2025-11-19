package com.meta.stock.product;

import com.meta.stock.product.Entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByComplete(int complete);

    // 최근 한 달간 가장 많이 주문된 상품 조회 (수량 기준)
    @Query(value =
            "SELECT * FROM (" +
                    "  SELECT o.PRODUCT_ID, SUM(o.QTY) as TOTAL_QTY " +
                    "  FROM ORDERS o " +
                    "  WHERE o.REQUEST_DATE >= TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYY-MM-DD') " +
                    "  GROUP BY o.PRODUCT_ID " +
                    "  ORDER BY TOTAL_QTY DESC" +
                    ") WHERE ROWNUM = 1",
            nativeQuery = true)
    List<Object[]> findTopProductLastMonth();
}