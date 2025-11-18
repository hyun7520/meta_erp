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
    private Integer orderId;

    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "REQUEST_BY")
    private String requestBy;

    @Column(name = "QTY")
    private Integer qty;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "REQUEST_DATE")
    private String requestDate;

    @Column(name = "DEADLINE")
    private String deadline;

    @Column(name = "COMPLETE")
    private Integer complete;  // Oracle: 0=false, 1=true

    // Getter
    public Integer getOrderId() {
        return orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public Integer getQty() {
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

    public Integer getComplete() {
        return complete;
    }

    // Setter
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public void setQty(Integer qty) {
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

    public void setComplete(Integer complete) {
        this.complete = complete;
    }
}