package com.meta.stock.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

// 제품 주문서
@Entity
@Table(name="Orders")
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
    private String requestDate;
    // 기한
    private String deadline;
    // 주문 완료 여부
    private boolean complete;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public int getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public boolean isComplete() {
        return complete;
    }
}
