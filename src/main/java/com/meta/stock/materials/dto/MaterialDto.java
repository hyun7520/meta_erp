package com.meta.stock.materials.dto;

public class MaterialDto {
    private String materialName;
    private int totalQty;
    private double avgLossRate;

    public String getMaterialName() {return materialName;}
    public int getTotalQty() {return totalQty;}
    public double getAvgLossRate() {return avgLossRate;}

    public void setMaterialName(String materialName) {this.materialName = materialName;}
    public void setTotalQty(int totalQty) {this.totalQty = totalQty;}
    public void setAvgLossRate(double avgLossRate) {this.avgLossRate = avgLossRate;}
}
