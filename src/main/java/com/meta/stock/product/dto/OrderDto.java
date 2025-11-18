package com.meta.stock.product.dto;

import com.meta.stock.product.entity.ProductEntity;

public class OrderDto {

    private int orderId;
    private ProductDto productDto;
    private String requestBy;
    private int qty;
    private int complete;
    private String unit;
    private String requestDate;
    private String deadline;

    public int getOrderId() {
        return orderId;
    }
    public ProductDto getProductDto() {
        return productDto;
    }
    public String getRequestBy() {
        return requestBy;
    }
    public int getQty() {
        return qty;
    }
    public int getComplete() {
        return complete;
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

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setComplete(int complete) {
        this.complete = complete;
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
