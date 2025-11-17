package com.meta.stock.product.entity;

import jakarta.persistence.*;

import java.util.Date;

// 제품 주문서
@Entity
@Table(name="Order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_gen")
    @SequenceGenerator(
            name = "order_gen",
            sequenceName = "ORDER_SEQ",
            allocationSize = 1
    )
    // 주문서 고유 번호
    private int orderId;
    // 재품 번호 - fixed_product 외래키
    private int productId;
    // 주문한 회사
    private String requestBy;
    // 주문 수량
    private int qty;
    // 주문 단위
    private String unit;
    // 요청 일자
    private Date requestDate;
    // 기한
    private Date deadline;
    // 주문 완료 여부
    private boolean complete;
}
