package com.meta.stock.materials.dto;

public class MaterialRequestDto {

    private long mrId;
    private long materialId;
    private String materialName;
    private int qty;
    private String requestDate;
    private int approved;
    private String approvedDate;
    private String note;

    public long getMrId() {
        return mrId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public int getQty() {
        return qty;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public int getApproved() {
        return approved;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public String getNote() {
        return note;
    }

    public void setMrId(long mrId) {
        this.mrId = mrId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
