package com.meta.stock.materials.entity;

import com.meta.stock.user.entity.EmployeeEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

// 재료 요청서 
@Entity
@Table(name = "MATERIAL_REQUEST")
public class MaterialRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mr_seq")
    @SequenceGenerator(name = "mr_seq", sequenceName = "MR_SEQ", allocationSize = 1)
    @Column(name = "mr_id")
    private long mrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_by")
    private EmployeeEntity requestBy;

    @Column(name = "request_date")
    private String requestDate;

    @Column(name = "qty")
    private int qty;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "approved")
    private int approved;  // 0:대기, 1:승인, 2:완료 등

    @Column(name = "approved_date")
    private String approvedDate;

    @Column(name = "note", length = 20)
    private String note;

    public long getMrId() {
        return mrId;
    }

    public EmployeeEntity getRequestBy() {
        return requestBy;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public int getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
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

    public void setRequestBy(EmployeeEntity requestBy) {
        this.requestBy = requestBy;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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