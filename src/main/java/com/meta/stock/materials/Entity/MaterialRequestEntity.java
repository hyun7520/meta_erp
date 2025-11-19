package com.meta.stock.materials.Entity;

import com.meta.stock.user.Employee;
import jakarta.persistence.*;
//생산팀에서 올린 원재료 발주 요청 데이터
//어떤 원재료를 (material_id)
//누가 요청했고 (request_by)
//얼마나 필요하며 (qty, unit)
//승인 상태는? (approved: null=미승인, 0=반려, 1=승인)


@Entity
@Table(name = "MATERIAL_REQUEST")
public class MaterialRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mr_seq")
    @SequenceGenerator(name = "mr_seq", sequenceName = "SEQ_MATERIAL_REQUEST", allocationSize = 1)
    @Column(name = "MR_ID")
    private long mrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATERIAL_ID")
    private MaterialEntity material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUEST_BY")
    private Employee requestBy;

    @Column(name = "REQUEST_DATE")
    private String requestDate;

    @Column(name = "QTY")
    private int qty;

    @Column(name = "APPROVED")
    private int approved;  // Oracle에서는 Boolean 대신 Integer (0, 1, null)
// int로 받고 if로 바꾸기

    @Column(name = "APPROVED_DATE")
    private String approvedDate;

    @Column(name = "NOTE")
    private String note;


    public long getMrId() {
        return mrId;
    }

    public void setMrId(long mrId) {
        this.mrId = mrId;
    }

    public MaterialEntity getMaterial() {
        return material;
    }

    public void setMaterial(MaterialEntity material) {
        this.material = material;
    }

    public Employee getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(Employee requestBy) {
        this.requestBy = requestBy;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }


    public int  getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}