package com.meta.stock.materials.dto;

public class MaterialRequirementDto {
    private Long fmId;
    private String materialName;
    private int requiredQty;
    private String unit;
    private int currentStock;
    private boolean sufficient;
    private int remainingQty = 0;

    public boolean isSufficient() {
        return currentStock > requiredQty;
    }

    public Long getFmId() {return fmId;}
    public String getMaterialName() {
        return materialName;
    }
    public int getRequiredQty() {
        return requiredQty;
    }
    public String getUnit() {
        return unit;
    }
    public int getCurrentStock() {
        return currentStock;
    }
    public int getRemainingQty() {
        return requiredQty - currentStock;
    }

    public void setFmId(Long fmId) {this.fmId = fmId;}
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    public void setRequiredQty(int requiredQty) {
        this.requiredQty = requiredQty;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }
    public void setRemainingQty(int remainingQty) {this.remainingQty = remainingQty;}
}