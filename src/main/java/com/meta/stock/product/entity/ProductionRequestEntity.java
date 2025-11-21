package com.meta.stock.product.entity;

import com.meta.stock.user.entity.EmployeeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTION_REQUEST")
public class ProductionRequestEntity {

    @Id
    @Column(name = "pr_id")
    private long prId;

    @Column(name = "management_employee")
    private long managementEmployee;

    @Column(name = "production_employee")
    private long productionEmployee;

    @Column(name = "to_company")
    private String toCompany;

    @Column(name = "target_qty", nullable = false)
    private Integer targetQty;

    @Column(name = "planned_qty")
    private int plannedQty;

    @Column(name = "completed_qty")
    private int completedQty;

    @Column(name = "unit")
    private String unit;

    @Column(name = "request_date")
    private String requestDate;

    @Column(name = "production_start_date")
    private String productionStartDate;

    @Column(name = "deadline")
    private String deadline;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "note", length = 255)
    private String note;

    public long getPrId() {
        return prId;
    }

    public long getManagementEmployee() {
        return managementEmployee;
    }

    public long getProductionEmployee() {
        return productionEmployee;
    }

    public String getToCompany() {
        return toCompany;
    }

    public Integer getTargetQty() {
        return targetQty;
    }

    public int getPlannedQty() {
        return plannedQty;
    }

    public int getCompletedQty() {
        return completedQty;
    }

    public String getUnit() {
        return unit;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getProductionStartDate() {
        return productionStartDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getNote() {
        return note;
    }

    public void setPrId(long prId) {
        this.prId = prId;
    }

    public void setManagementEmployee(long managementEmployee) {
        this.managementEmployee = managementEmployee;
    }

    public void setProductionEmployee(long productionEmployee) {
        this.productionEmployee = productionEmployee;
    }

    public void setToCompany(String toCompany) {
        this.toCompany = toCompany;
    }

    public void setTargetQty(Integer targetQty) {
        this.targetQty = targetQty;
    }

    public void setPlannedQty(int plannedQty) {
        this.plannedQty = plannedQty;
    }

    public void setCompletedQty(int completedQty) {
        this.completedQty = completedQty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public void setProductionStartDate(String productionStartDate) {
        this.productionStartDate = productionStartDate;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setNote(String note) {
        this.note = note;
    }
}