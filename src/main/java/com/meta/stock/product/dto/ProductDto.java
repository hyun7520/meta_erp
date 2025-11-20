package com.meta.stock.product.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.util.Date;

public class ProductDto {

    private long productId;
    private String productName;
    private float productionLoss;
    private long prId;
    private long lotsId;

    public long getProductId() {return productId;}
    public String getProductName() {return productName;}
    public float getProductionLoss() {return productionLoss;}
    public long getPrId() {return prId;}
    public long getLotsId() {return lotsId;}

    public void setProductId(long productId) {this.productId = productId;}
    public void setProductName(String productName) {this.productName = productName;}
    public void setProductionLoss(float productionLoss) {this.productionLoss = productionLoss;}
    public void setPrId(long prId) {this.prId = prId;}
    public void setLotsId(long lotsId) {this.lotsId = lotsId;}
}
