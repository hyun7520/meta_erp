package com.meta.stock.product;

public class ProductDTO {
    private Long productId;
    private String productName;
    private Long lotsId;
    private Integer productionLoss;

    // Getter
    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getLotsId() {
        return lotsId;
    }

    public Integer getProductionLoss() {
        return productionLoss;
    }

    // Setter
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setLotsId(Long lotsId) {
        this.lotsId = lotsId;
    }

    public void setProductionLoss(Integer productionLoss) {
        this.productionLoss = productionLoss;
    }
}