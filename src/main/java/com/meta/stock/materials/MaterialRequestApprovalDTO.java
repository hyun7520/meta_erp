package com.meta.stock.materials;

public class MaterialRequestApprovalDTO {
    private Long mrId;
    private Integer approved; // true: 승인, false: 반려
    private String note; // 승인/반려 사유

    // Getter
    public Long getMrId() {
        return mrId;
    }

    public Integer getApproved() {
        return approved;
    }

    public String getNote() {
        return note;
    }

    // Setter
    public void setMrId(Long mrId) {
        this.mrId = mrId;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public void setNote(String note) {
        this.note = note;
    }
}