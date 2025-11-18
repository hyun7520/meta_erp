package com.meta.stock.order.entity;

import com.meta.stock.product.entity.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "ORDERS")   // 예약어 회피 + 복수형
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_gen")
    @SequenceGenerator(name = "order_gen", sequenceName = "ORDER_SEQ", allocationSize = 1)
    @Column(name = "ORDER_ID")
    private int orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private ProductEntity product;

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
    private boolean complete;

    public int getOrderId() {
        return orderId;
    }

    public ProductEntity getProduct() {
        return product;
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

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
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
}
