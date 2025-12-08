package com.meta.stock.materials.dto;

import java.util.List;

public class MaterialRequestFormDto {
    private List<MaterialRequestDto> materialRequests;
    private String requestDate;
    private String requestBy;

    // Getters and Setters
    public List<MaterialRequestDto> getMaterialRequests() {
        return materialRequests;
    }
    public String getRequestDate() {
        return requestDate;
    }
    public String getRequestBy() {
        return requestBy;
    }

    public void setMaterialRequests(List<MaterialRequestDto> materialRequests) {
        this.materialRequests = materialRequests;
    }
    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }
}
