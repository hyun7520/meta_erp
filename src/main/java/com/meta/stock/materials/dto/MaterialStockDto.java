package com.meta.stock.materials.dto;

public class MaterialStockDto {
    private String materialName;
    private Integer qty;
    private String unit;

    public String getMaterialName() {return materialName;}
    public Integer getQty() {return qty != null ? qty : 0;}
    public String getUnit() {return unit;}

    public void setMaterialName(String materialName) {this.materialName = materialName;}
    public void setQty(Integer qty) {this.qty = qty;}
    public void setUnit(String unit) {this.unit = unit;}
}