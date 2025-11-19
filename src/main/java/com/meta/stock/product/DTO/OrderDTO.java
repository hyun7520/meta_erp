package com.meta.stock.product.DTO;

public class OrderDTO {
    private long orderId;
    private String productName;
    private String requestBy;
    private int qty;
    private String unit;
    private String requestDate;
    private String deadline;
    private int complete;
    private String status; // "대기중", "진행중", "완료"

    // Getter
    public long getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
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

    public int getComplete() {
        return complete;
    }

    public String getStatus() {
        return status;
    }

    // Setter
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}