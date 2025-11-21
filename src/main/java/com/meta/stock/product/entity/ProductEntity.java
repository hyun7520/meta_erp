package com.meta.stock.product.entity;

import jakarta.persistence.*;

// 생산된 제품
@Entity
@Table(name = "PRODUCTS")
public class ProductEntity {

    @Id
    @Column(name = "product_id")
    private long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "production_loss")
    private int productionLoss;

    @Column(name = "pr_id")
    private long prId;

    @Column(name = "lots_id")
    private long lotsId;

    // getter & setter
    public long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getProductionLoss() {return productionLoss;}
    public long getPrId() {return prId;}
    public long getLotsId() { return lotsId; }

    public void setProductId(long productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setProductionLoss(int productionLoss) {this.productionLoss = productionLoss;}
    public void setPrId(long prId) {this.prId = prId;}
    public void setLotsId(long lotsId) { this.lotsId = lotsId; }
}