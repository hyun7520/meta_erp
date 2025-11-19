package com.meta.stock.product.DTO;

public class OrderCreateDTO {
    private int productId;
    private String requestBy;
    private int qty;
    private String unit;
    private String requestDate;
    private String deadline;

    // Getter
    public int getProductId() {
        return productId;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public int getQty() {
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
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public void setQty(int qty) {
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