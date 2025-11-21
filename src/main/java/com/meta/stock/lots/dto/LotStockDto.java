package com.meta.stock.lots.dto;

public class LotStockDto {
    private Long lotId;
    private Long productId;
    private String productName;
    private Integer qty;
    private String unit;
    private String storageDate;
    private String shelfLifeDays;  // 유통기한
    private String note;

    public Long getLotId() {return lotId;}
    public Long getProductId() {return productId;}
    public String getProductName() {return productName;}
    public Integer getQty() {return qty;}
    public String getUnit() {return unit;}
    public String getStorageDate() {return storageDate;}
    public String getShelfLifeDays() {return shelfLifeDays;}
    public String getNote() {return note;}

    public void setLotId(Long lotId) {this.lotId = lotId;}
    public void setProductId(Long productId) {this.productId = productId;}
    public void setProductName(String productName) {this.productName = productName;}
    public void setQty(Integer qty) {this.qty = qty;}
    public void setUnit(String unit) {this.unit = unit;}
    public void setStorageDate(String storageDate) {this.storageDate = storageDate;}
    public void setShelfLifeDays(String shelfLifeDays) {this.shelfLifeDays = shelfLifeDays;}
    public void setNote(String note) {this.note = note;}
}