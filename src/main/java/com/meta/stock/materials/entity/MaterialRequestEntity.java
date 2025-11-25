package com.meta.stock.materials.entity;

import jakarta.persistence.*;
import lombok.Builder;

// 재료 요청서 
@Entity
@Table(name = "MATERIAL_REQUEST")
public class MaterialRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mr_seq")
    @SequenceGenerator(name = "mr_seq", sequenceName = "SEQ_MATERIAL_REQUEST", allocationSize = 1)
    @Column(name = "MR_ID")
    private long mrId;

    @Column(name = "REQUEST_BY")
    private long requestBy; // 요청자 Employee ID

    @Column(name = "MANAGEMENT_EMPLOYEE")
    private long managementEmployee; // 경영 담당자 Employee ID

    @Column(name = "PRODUCTION_EMPLOYEE")
    private long productionEmployee; // 생산 담당자 Employee ID

    @Column(name = "REQUEST_DATE")
    private String requestDate;

    @Column(name = "QTY")
    private int qty;

    @Column(name = "UNIT", length = 20)
    private String unit;

    @Column(name = "APPROVED")
    private int approved; // 0: 미승인, 1: 승인, -1: 반려

    @Column(name = "APPROVED_DATE")
    private String approvedDate;

    @Column(name = "NOTE", length = 255)
    private String note;

    public long getMrId() {
        return mrId;
    }

    public long getRequestBy() {
        return requestBy;
    }

    public long getManagementEmployee() {
        return managementEmployee;
    }

    public long getProductionEmployee() {
        return productionEmployee;
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

    public void setRequestBy(long requestBy) {
        this.requestBy = requestBy;
    }

    public void setManagementEmployee(long managementEmployee) {
        this.managementEmployee = managementEmployee;
    }

    public void setProductionEmployee(long productionEmployee) {
        this.productionEmployee = productionEmployee;
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