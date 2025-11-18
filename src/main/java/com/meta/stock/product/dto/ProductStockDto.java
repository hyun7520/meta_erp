package com.meta.stock.product.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductStockDto {

    private long productId;
    private String productName;
    private long lotId;
    private int qty;               // 현재 재고 수량
    private String unit;
    private String storageDate;    // 입고일
    private String shelfLifeDays;  // 유통기한
    private String note;

    public long getProductId() {return productId;}
    public String getProductName() {return productName;}
    public long getLotId() {return lotId;}
    public int getQty() {return qty;}
    public String getUnit() {return unit;}
    public String getStorageDate() {return storageDate;}
    public String getShelfLifeDays() {return shelfLifeDays;}
    public String getNote() {return note;}

    public void setProductId(long productId) {this.productId = productId;}
    public void setProductName(String productName) {this.productName = productName;}
    public void setLotId(long lotId) {this.lotId = lotId;}
    public void setQty(int qty) {this.qty = qty;}
    public void setUnit(String unit) {this.unit = unit;}
    public void setStorageDate(String storageDate) {this.storageDate = storageDate;}
    public void setShelfLifeDays(String shelfLifeDays) {this.shelfLifeDays = shelfLifeDays;}
    public void setNote(String note) {this.note = note;}
}
