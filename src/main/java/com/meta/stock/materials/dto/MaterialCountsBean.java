package com.meta.stock.materials.dto;

public class MaterialCountsBean {
    private String materialName;
    private int qty;

    public String getMaterialName() {
        return materialName;
    }

    public int getQty() {
        return qty;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
