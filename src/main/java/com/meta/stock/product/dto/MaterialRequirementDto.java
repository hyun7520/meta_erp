package com.meta.stock.product.dto;

public class MaterialRequirementDto {
    private String materialName;
    private Integer requiredQty;
    private Integer currentStock;
    private String unit;

    // 충분 여부
    public boolean isSufficient() {
        return currentStock >= requiredQty;
    }

    // 남은 필요량
    public int getRemainingQty() {
        return Math.max(0, requiredQty - currentStock);
    }

    public String getMaterialName() {return materialName;}
    public Integer getRequiredQty() {return requiredQty;}
    public Integer getCurrentStock() {return currentStock;}
    public String getUnit() {return unit;}

    public void setMaterialName(String materialName) {this.materialName = materialName;}
    public void setRequiredQty(Integer requiredQty) {this.requiredQty = requiredQty;}
    public void setCurrentStock(Integer currentStock) {this.currentStock = currentStock;}
    public void setUnit(String unit) {this.unit = unit;}
}
