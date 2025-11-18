package com.meta.stock.product.dto;

import java.util.Date;

public class ProductDto {

    private long productId;
    private String productName;
    private long lotsId;

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public long getLotsId() {
        return lotsId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setLotsId(long lotsId) {
        this.lotsId = lotsId;
    }
}
