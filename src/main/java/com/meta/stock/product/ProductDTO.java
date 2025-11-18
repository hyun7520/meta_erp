package com.meta.stock.product;

public class ProductDTO {
    private long productId;
    private String productName;
    private long lotsId;
    private int productionLoss;

    // Getter
    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public long getLotsId() {
        return lotsId;
    }

    public int getProductionLoss() {
        return productionLoss;
    }

    // Setter
    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setLotsId(long lotsId) {
        this.lotsId = lotsId;
    }

    public void setProductionLoss(int productionLoss) {
        this.productionLoss = productionLoss;
    }
}