package com.meta.stock.product.dto;

public class ProductDemandBean {
    private String name;
    private String date;
    private float demandAmount;

    public ProductDemandBean(String name, String date, float demandAmount) {
        this.name = name;
        this.date = date;
        this.demandAmount = demandAmount;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public float getDemandAmount() {
        return demandAmount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDemandAmount(float demandAmount) {
        this.demandAmount = demandAmount;
    }
}
