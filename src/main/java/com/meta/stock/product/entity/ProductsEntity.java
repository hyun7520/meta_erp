package com.meta.stock.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "'Products'")
public class ProductsEntity {
    @Id
    private int productId;
    private String productName;
    private int productionLoss;

    private int lotsId;

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setLotsId(int lotsId) {
        this.lotsId = lotsId;
    }

    public void setProductionLoss(int productionLoss) {
        this.productionLoss = productionLoss;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getLotsId() {
        return lotsId;
    }

    public int getProductionLoss() {
        return productionLoss;
    }
}
