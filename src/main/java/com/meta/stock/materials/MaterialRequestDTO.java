package com.meta.stock.materials;

import java.time.LocalDate;

public class MaterialRequestDTO {
    private Long mrId;
    private String materialName;
    private String requestByName;
    private String requestDate;
    private Integer qty;
    private String unit;
    private Integer approved;
    private String approvedDate;
    private String note;

    // Getter
    public Long getMrId() {
        return mrId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getRequestByName() {
        return requestByName;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public Integer getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public Integer getApproved() {
        return approved;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public String getNote() {
        return note;
    }

    // Setter
    public void setMrId(Long mrId) {
        this.mrId = mrId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setRequestByName(String requestByName) {
        this.requestByName = requestByName;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public void setNote(String note) {
        this.note = note;
    }
}