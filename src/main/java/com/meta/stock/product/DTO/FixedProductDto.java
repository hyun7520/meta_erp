package com.meta.stock.product.DTO;

import com.meta.stock.materials.dto.MaterialDto;

import java.util.List;

public class FixedProductDto {
    private Long fpId;
    private String serialCode;
    private String name;
    private int currentStock;
    private List<MaterialDto> requiredMaterials;

    public Long getFpId() {return fpId;}
    public String getSerialCode() {return serialCode;}
    public String getName() {return name;}
    public int getCurrentStock() {return currentStock;}
    public List<MaterialDto> getRequiredMaterials() {return requiredMaterials;}

    public void setFpId(Long fpId) {this.fpId = fpId;}
    public void setSerialCode(String serialCode) {this.serialCode = serialCode;}
    public void setName(String name) {this.name = name;}
    public void setCurrentStock(int currentStock) {this.currentStock = currentStock;}
    public void setRequiredMaterials(List<MaterialDto> requiredMaterials) {this.requiredMaterials = requiredMaterials;}

    @Override
    public String toString() {
        String temp = "";
        for(MaterialDto materialDto : requiredMaterials) {
            temp += materialDto.toString();
        }
        return "fpId=" + fpId +
                ", serialCode=" + serialCode +
                ", name='" + name + '\'' +
                ", currentStock=" + currentStock +
                ", materials" + temp;
    }
}
