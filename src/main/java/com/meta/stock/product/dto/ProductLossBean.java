package com.meta.stock.product.dto;

public class ProductLossBean {
    private String caseName; // 제품 SerialCode
    private String type; // 제품명
    private String date; // yyyy-MM 형식
    private float percent; // 로스율 / 습도 퍼센트

    public ProductLossBean() {}
    public ProductLossBean(String caseName, String type, String date, float percent) {
        this.caseName = caseName;
        this.type = type;
        this.date = date;
        this.percent = percent;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public float getPercent() {
        return percent;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
