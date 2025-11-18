package com.meta.stock.product;

public class OrderDTO {
    private Integer orderId;
    private String productName;
    private String requestBy;
    private Integer qty;
    private String unit;
    private String requestDate;
    private String deadline;
    private Integer complete;
    private String status; // "대기중", "진행중", "완료"

    // Getter
    public Integer getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
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

    public Integer getComplete() {
        return complete;
    }

    public String getStatus() {
        return status;
    }

    // Setter
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}