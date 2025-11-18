package com.meta.stock.product.entity;

import jakarta.persistence.*;

// 생산된 제품
@Entity
@Table(name = "PRODUCT")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    @SequenceGenerator(name = "product_gen", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "LOTS_ID")
    private long lotsId;

    // getter & setter
    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public long getLotsId() { return lotsId; }
    public void setLotsId(long lotsId) { this.lotsId = lotsId; }
}