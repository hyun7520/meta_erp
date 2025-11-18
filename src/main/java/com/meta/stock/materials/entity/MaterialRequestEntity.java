package com.meta.stock.materials.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

// 재료 요청서 
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

    @Column(name = "REQUEST_DATE")
    private LocalDate requestDate;

    @Column(name = "QTY")
    private Integer qty;

    @Column(name = "APPROVED")
    private int approved;   // Oracle은 boolean 대신 0/1 사용

    @Column(name = "APPROVED_DATE")
    private String approvedDate;   // 또는 LocalDate로 변경 가능

    @Column(name = "NOTE")
    private String note;

    // getter & setter
    public Long getMrId() { return mrId; }
    public void setMrId(Long mrId) { this.mrId = mrId; }
    public MaterialEntity getMaterial() { return material; }
    public void setMaterial(MaterialEntity material) { this.material = material; }
    public LocalDate getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }
    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
    public int getApproved() { return approved; }
    public void setApproved(int approved) { this.approved = approved; }
    public String getApprovedDate() { return approvedDate; }
    public void setApprovedDate(String approvedDate) { this.approvedDate = approvedDate; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}