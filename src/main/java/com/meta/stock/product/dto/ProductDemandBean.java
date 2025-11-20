package com.meta.stock.product.dto;

public class ProductDemandBean {
    private String name;
    private String requestDate;
    private float demand;

    public ProductDemandBean(String name, String requestDate, float demand) {
        this.name = name;
        this.requestDate = requestDate;
        this.demand = demand;
    }

    public String getName() {
        return name;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public float getDemand() {
        return demand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public void setDemand(float demand) {
        this.demand = demand;
    }
}
