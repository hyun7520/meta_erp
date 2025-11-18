package com.meta.stock.materials.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "MATERIAL_REQUEST")
public class MaterialRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mr_seq")
    @SequenceGenerator(name = "mr_seq", sequenceName = "SEQ_MATERIAL_REQUEST", allocationSize = 1)
    @Column(name = "MR_ID")
    private Long mrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATERIAL_ID")
    private MaterialEntity material;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "REQUEST_BY")
//    private Employee requestBy;

    @Column(name = "REQUEST_DATE")
    private String requestDate;

    @Column(name = "QTY")
    private Integer qty;

    @Column(name = "APPROVED")
    private Integer approved;  // Oracle에서는 Boolean 대신 Integer (0, 1, null)
// int로 받고 if로 바꾸기

    @Column(name = "APPROVED_DATE")
    private String approvedDate;

    @Column(name = "NOTE")
    private String note;

    public void setMrId(Long mrId) {
        this.mrId = mrId;
    }

    public void setMaterial(MaterialEntity material) {
        this.material = material;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
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

    public Long getMrId() {
        return mrId;
    }

    public MaterialEntity getMaterial() {
        return material;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public Integer getQty() {
        return qty;
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
}