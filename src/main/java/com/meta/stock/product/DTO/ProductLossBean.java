package com.meta.stock.product.dto;

public class ProductLossBean {
    private String serialCode; // 제품 SerialCode
    private String productName; // 제품명
    private String[] loss; // case별 로스율

    public String getSerialCode() {
        return serialCode;
    }

    public String getProductName() {
        return productName;
    }

    public String[] getLoss() {
        return loss;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setLoss(String[] loss) {
        this.loss = loss;
    }
}
