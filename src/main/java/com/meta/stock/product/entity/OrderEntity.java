package com.meta.stock.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name="Order")
public class OrderEntity {

    private int orderId;
    private int productId;
    private String requestBy;
    private int qty;
    private String unit;
    private Date requestDate;
    private Date deadline;
    private boolean complete;
}
