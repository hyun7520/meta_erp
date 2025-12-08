package com.meta.stock.product.dto;

public class TopProductDTO {
    private String productName;
    private int totalQty;
    private int orderCount;
    private String unit;

    // 생성자
    public TopProductDTO(String productName, int totalQty, int orderCount, String unit) {
        this.productName = productName;
        this.totalQty = totalQty;
        this.orderCount = orderCount;
        this.unit = unit;
    }

    // Getter
    public String getProductName() {
        return productName;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public String getUnit() {
        return unit;
    }

    // Setter
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}