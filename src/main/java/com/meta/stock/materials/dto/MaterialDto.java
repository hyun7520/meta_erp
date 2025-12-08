package com.meta.stock.materials.dto;

public class MaterialDto {
    private Long fmId;
    private String name;
    private int qty;              // 제품 1개당 필요한 재료량
    private String unit;
    private int currentStock;      // 현재 재료 재고
    private boolean sufficient;       // 충족 여부

    public Long getFmId() {
        return fmId;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public boolean isSufficient() {
        return sufficient;
    }

    public void setFmId(Long fmId) {
        this.fmId = fmId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public void setSufficient(boolean sufficient) {
        this.sufficient = sufficient;
    }

    @Override
    public String toString() {
        return "MaterialDto{" +
                "fmId=" + fmId +
                ", name='" + name + '\'' +
                ", qty=" + qty +
                ", unit='" + unit + '\'' +
                ", currentStock=" + currentStock +
                ", sufficient=" + sufficient +
                '}';
    }
}
