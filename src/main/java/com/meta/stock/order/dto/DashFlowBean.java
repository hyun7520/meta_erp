package com.meta.stock.order.dto;

public class DashFlowBean {
    private int orderRequest;
    private int metrialRequest;
    private int metrialRequestClear;
    private int productionReady;
    private int todayDelivered;

    public int getOrderRequest() {
        return orderRequest;
    }

    public int getMetrialRequest() {
        return metrialRequest;
    }

    public int getMetrialRequestClear() {
        return metrialRequestClear;
    }

    public int getProductionReady() {
        return productionReady;
    }

    public int getTodayDelivered() {
        return todayDelivered;
    }

    public void setOrderRequest(int orderRequest) {
        this.orderRequest = orderRequest;
    }

    public void setMetrialRequest(int metrialRequest) {
        this.metrialRequest = metrialRequest;
    }

    public void setMetrialRequestClear(int metrialRequestClear) {
        this.metrialRequestClear = metrialRequestClear;
    }

    public void setProductionReady(int productionReady) {
        this.productionReady = productionReady;
    }

    public void setTodayDelivered(int todayDelivered) {
        this.todayDelivered = todayDelivered;
    }
}
