package com.meta.stock.product.dto;

public class ProductsAmountListBean {
    private int productId;
    private String serialCode;
    private String productName;
    private int qty;
    private String unit;
    private String storageDate;
    private String shelfLifeDays;

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setStorageDate(String storageDate) {
        this.storageDate = storageDate;
    }

    public void setShelfLifeDays(String shelfLifeDays) {
        this.shelfLifeDays = shelfLifeDays;
    }

    public int getProductId() {
        return productId;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public String getProductName() {
        return productName;
    }

    public int getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public String getStorageDate() {
        return storageDate;
    }

    public String getShelfLifeDays() {
        return shelfLifeDays;
    }
}
