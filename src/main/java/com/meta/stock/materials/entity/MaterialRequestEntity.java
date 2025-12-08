package com.meta.stock.materials.entity;

import com.meta.stock.config.DateStringConverter;
import jakarta.persistence.*;

// 재료 요청서 
@Entity
@Table(name = "MATERIAL_REQUEST")
public class MaterialRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mr_seq")
    @SequenceGenerator(name = "mr_seq", sequenceName = "SEQ_MATERIAL_REQUEST", allocationSize = 1)
    @Column(name = "MR_ID")
    private Long mrId;

    @Column(name = "REQUEST_BY")
    private Long requestBy; // 요청자 Employee ID

    @Column(name = "MANAGEMENT_EMPLOYEE")
    private Long managementEmployee; // 경영 담당자 Employee ID

    @Column(name = "PRODUCTION_EMPLOYEE")
    private Long productionEmployee; // 생산 담당자 Employee ID

    @Column(name = "REQUEST_DATE")
    @Convert(converter = DateStringConverter.class)
    private String requestDate;

    @Column(name = "QTY")
    private int qty;

    @Column(name = "UNIT", length = 20)
    private String unit;

    @Column(name = "APPROVED")
    private int approved; // 0: 미승인, 1: 승인, -1: 반려

    @Column(name = "APPROVED_DATE")
    @Convert(converter = DateStringConverter.class)
    private String approvedDate;

    @Column(name = "NOTE", length = 255)
    private String note;

    @Column(name = "fm_id")
    private Long fmId;

    public Long getMrId() {
        return mrId;
    }

    public Long getRequestBy() {
        return requestBy;
    }

    public Long getManagementEmployee() {
        return managementEmployee;
    }

    public Long getProductionEmployee() {
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

    public Long getFmId() {
        return fmId;
    }

    public void setMrId(Long mrId) {
        this.mrId = mrId;
    }

    public void setRequestBy(Long requestBy) {
        this.requestBy = requestBy;
    }

    public void setManagementEmployee(Long managementEmployee) {
        this.managementEmployee = managementEmployee;
    }

    public void setProductionEmployee(Long productionEmployee) {
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

    public void setFmId(Long fmId) {
        this.fmId = fmId;
    }
}