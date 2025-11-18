package com.meta.stock.product;

import jakarta.persistence.*;

@Entity
@Table(name = "ORDERS")  // 테이블 이름 대문자
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_gen")
    @SequenceGenerator(
            name = "order_gen",
            sequenceName = "SEQ_ORDERS",
            allocationSize = 1
    )
    @Column(name = "ORDER_ID")
    private long orderId;

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "REQUEST_BY")
    private String requestBy;

    @Column(name = "QTY")
    private int qty;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "REQUEST_DATE")
    private String requestDate;

    @Column(name = "DEADLINE")
    private String deadline;

    @Column(name = "COMPLETE")
    private int complete;  // Oracle: 0=요청 완료, 1=승낙(제조시작) 2= 출하

    // Getter
    public long getOrderId() {
        return orderId;
    }

    public long getProductId() {
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

    public int getComplete() {
        return complete;
    }

    // Setter
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setProductId(long productId) {
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

    public void setComplete(int complete) {
        this.complete = complete;
    }
}