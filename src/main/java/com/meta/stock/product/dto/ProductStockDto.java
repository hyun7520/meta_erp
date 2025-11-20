package com.meta.stock.product.dto;

public class ProductStockDto {

    private long productId;
    private String productName;
    private long lotId;
    private int qty;               // 현재 재고 수량
    private String unit;
    private String storageDate;    // 입고일
    private String shelfLifeDays;  // 유통기한
    private Long daysLeft;
    private String note;

    // 임박 여부
    public boolean isExpiringSoon() {
        return daysLeft != null && daysLeft <= 7 && daysLeft >= 0;
    }

    // 만료 여부
    public boolean isExpired() {
        return daysLeft != null && daysLeft < 0;
    }

    // 상태 텍스트
    public String getStatusText() {
        if (daysLeft == null) return "정보없음";
        if (daysLeft < 0) return "만료";
        if (daysLeft == 0) return "오늘";
        if (daysLeft <= 7) return "임박";
        return "정상";
    }

    // 상태 뱃지 클래스
    public String getStatusBadgeClass() {
        if (daysLeft == null) return "badge bg-secondary";
        if (daysLeft < 0) return "badge bg-danger";
        if (daysLeft <= 7) return "badge badge-expiring";
        return "badge bg-success";
    }

    public long getProductId() {return productId;}
    public String getProductName() {return productName;}
    public long getLotId() {return lotId;}
    public int getQty() {return qty;}
    public String getUnit() {return unit;}
    public String getStorageDate() {return storageDate;}
    public String getShelfLifeDays() {return shelfLifeDays;}
    public Long getDaysLeft() {return daysLeft;}
    public String getNote() {return note;}

    public void setProductId(long productId) {this.productId = productId;}
    public void setProductName(String productName) {this.productName = productName;}
    public void setLotId(long lotId) {this.lotId = lotId;}
    public void setQty(int qty) {this.qty = qty;}
    public void setUnit(String unit) {this.unit = unit;}
    public void setStorageDate(String storageDate) {this.storageDate = storageDate;}
    public void setShelfLifeDays(String shelfLifeDays) {this.shelfLifeDays = shelfLifeDays;}
    public void setDaysLeft(Long daysLeft) {this.daysLeft = daysLeft;}
    public void setNote(String note) {this.note = note;}
}


