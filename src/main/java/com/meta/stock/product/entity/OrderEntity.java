package com.meta.stock.product.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Table(name = "ORDERS")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_gen")
    @SequenceGenerator(name = "order_gen", sequenceName = "ORDER_SEQ", allocationSize = 1)
    @Column(name = "ORDER_ID")
    private long orderId;

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
    @ColumnDefault("0")
    private int complete;

    // getter & setter
    public long getOrderId() { return orderId; }
    public ProductEntity getProduct() { return product; }
    public String getRequestBy() { return requestBy; }
    public int getQty() { return qty; }
    public String getUnit() { return unit; }
    public String getRequestDate() { return requestDate; }
    public String getDeadline() { return deadline; }
    public int getComplete() { return complete; }

    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setProductId(ProductEntity product) { this.product = product; }
    public void setRequestBy(String requestBy) { this.requestBy = requestBy; }
    public void setQty(int qty) { this.qty = qty; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public void setComplete(int complete) { this.complete = complete; }
}
