package com.meta.stock.product.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class ProductRequestDto {

    private Long prId;
    private Long managementEmployee;
    private Long productionEmployee;
    private String serialCode;
    private String productName;  // Fixed_Product의 name
    private String toCompany;
    private Integer targetQty;
    private Integer plannedQty;
    private Integer completedQty;
    private Integer inStockQty;
    private String unit;
    private String requestDate;
    private String productionStartDate;
    private String deadline;
    private String endDate;
    private String note;

    public boolean isStockSufficient() {
        return getInStockQty() + getCompletedQty() >= getTargetQty();
    }

    public int getComplete() {
        if (productionStartDate == null) {
            return 0;  // 미수주
        } else if (endDate == null) {
            return 1;  // 수주 완료 (진행 중)
        } else {
            return 2;  // 완료
        }
    }

    public long getPrId() {return prId;}
    public long getManagementEmployee() {return managementEmployee;}
    public long getProductionEmployee() {return productionEmployee;}
    public String getSerialCode() {return serialCode;}
    public String getProductName() {return productName;}
    public String getToCompany() {return toCompany;}
    public int getTargetQty() {return targetQty;}
    public Integer getPlannedQty() {return plannedQty != null ? plannedQty : 0;}
    public Integer getCompletedQty() {return completedQty != null ? completedQty : 0;}

    public Integer getInStockQty() {return inStockQty;}
    public String getUnit() {return unit;}
    public String getRequestDate() {return requestDate;}
    public String getProductionStartDate() {return productionStartDate;}
    public String getDeadline() {return deadline;}
    public String getEndDate() {return endDate;}
    public String getNote() {return note;}

    public void setPrId(long prId) {this.prId = prId;}
    public void setManagementEmployee(long managementEmployee) {this.managementEmployee = managementEmployee;}
    public void setProductionEmployee(long productionEmployee) {this.productionEmployee = productionEmployee;}
    public void setSerialCode(String serialCode) {this.serialCode = serialCode;}
    public void setProductName(String productName) {this.productName = productName;}
    public void setToCompany(String toCompany) {this.toCompany = toCompany;}
    public void setTargetQty(int targetQty) {this.targetQty = targetQty;}
    public void setPlannedQty(Integer plannedQty) {this.plannedQty = plannedQty;}
    public void setCompletedQty(Integer completedQty) {this.completedQty = completedQty;}

    public void setInStockQty(Integer inStockQty) {this.inStockQty = inStockQty;}

    public void setUnit(String unit) {this.unit = unit;}
    public void setRequestDate(String requestDate) {this.requestDate = requestDate;}
    public void setProductionStartDate(String productionStartDate) {this.productionStartDate = productionStartDate;}
    public void setDeadline(String deadline) {this.deadline = deadline;}
    public void setEndDate(String endDate) {this.endDate = endDate;}
    public void setNote(String note) {this.note = note;}
}
