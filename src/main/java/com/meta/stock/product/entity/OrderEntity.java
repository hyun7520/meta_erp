package com.meta.stock.product.entity;

import jakarta.persistence.*;

import java.util.Date;

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
    private Date requestDate;

    @Column(name = "DEADLINE")
    private Date deadline;

    @Column(name = "COMPLETE")
    private boolean complete;

    // getter & setter
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public ProductEntity getProduct() { return product; }
    public void setProductId(ProductEntity product) { this.product = product; }
    public String getRequestBy() { return requestBy; }
    public void setRequestBy(String requestBy) { this.requestBy = requestBy; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Date getRequestDate() { return requestDate; }
    public void setRequestDate(Date requestDate) { this.requestDate = requestDate; }
    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }
    public boolean isComplete() { return complete; }
    public void setComplete(boolean complete) { this.complete = complete; }
}
