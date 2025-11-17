package com.meta.stock.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "'Fixed_Product'")
public class FixedProductEntity {
    @Id
    private String productId;
    private String serialCode;
    private String name;
    private int productionTime;

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductionTime(int productionTime) {
        this.productionTime = productionTime;
    }

    public String getProductId() {
        return productId;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public String getName() {
        return name;
    }

    public int getProductionTime() {
        return productionTime;
    }
}
