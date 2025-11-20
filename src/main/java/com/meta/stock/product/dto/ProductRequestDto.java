package com.meta.stock.product.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class ProductRequestDto {

    private long prId;
    private long managementEmployee;
    private long productionEmployee;
    private String toCompany;
    private int targetQty;
    private int plannedQty;
    private int completedQty;
    private String unit;
    private String requestDate;
    private String productionStartDate;
    private String deadline;
    private String endDate;
    private String note;

    public long getPrId() {return prId;}
    public long getManagementEmployee() {return managementEmployee;}
    public long getProductionEmployee() {return productionEmployee;}
    public String getToCompany() {return toCompany;}
    public int getTargetQty() {return targetQty;}
    public int getPlannedQty() {return plannedQty;}
    public int getCompletedQty() {return completedQty;}
    public String getUnit() {return unit;}
    public String getRequestDate() {return requestDate;}
    public String getProductionStartDate() {return productionStartDate;}
    public String getDeadline() {return deadline;}
    public String getEndDate() {return endDate;}
    public String getNote() {return note;}

    public void setPrId(long prId) {this.prId = prId;}
    public void setManagementEmployee(long managementEmployee) {this.managementEmployee = managementEmployee;}
    public void setProductionEmployee(long productionEmployee) {this.productionEmployee = productionEmployee;}
    public void setToCompany(String toCompany) {this.toCompany = toCompany;}
    public void setTargetQty(int targetQty) {this.targetQty = targetQty;}
    public void setPlannedQty(int plannedQty) {this.plannedQty = plannedQty;}
    public void setCompletedQty(int completedQty) {this.completedQty = completedQty;}
    public void setUnit(String unit) {this.unit = unit;}
    public void setRequestDate(String requestDate) {this.requestDate = requestDate;}
    public void setProductionStartDate(String productionStartDate) {this.productionStartDate = productionStartDate;}
    public void setDeadline(String deadline) {this.deadline = deadline;}
    public void setEndDate(String endDate) {this.endDate = endDate;}
    public void setNote(String note) {this.note = note;}
}
