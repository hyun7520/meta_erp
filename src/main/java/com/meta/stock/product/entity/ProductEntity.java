package com.meta.stock.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Product")
public class ProductEntity {

    private int productId;
    private String productName;
    private int lotsId;
    private float productionLoss;
}
