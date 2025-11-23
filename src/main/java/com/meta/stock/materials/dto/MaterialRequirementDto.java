package com.meta.stock.materials.dto;

public class MaterialRequirementDto {
    private String materialName;
    private int requiredQty;
    private String unit;
    private int currentStock;
    private boolean sufficient;
    private int remainingQty = 0;

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

    public boolean isSufficient() {
        return currentStock > requiredQty;
    }

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
}