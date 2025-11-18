package com.meta.stock.product;

public class OrderCreateDTO {
    private Integer productId;
    private String requestBy;
    private Integer qty;
    private String unit;
    private String requestDate;
    private String deadline;

    // Getter
    public Integer getProductId() {
        return productId;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public Integer getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getDeadline() {
        return deadline;
    }

    // Setter
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}