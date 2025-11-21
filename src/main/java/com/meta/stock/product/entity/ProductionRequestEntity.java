package com.meta.stock.product.entity;

import com.meta.stock.config.DateStringConverter;  // 추가
import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTION_REQUEST")
public class ProductionRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pr_seq")
    @SequenceGenerator(name = "pr_seq", sequenceName = "SEQ_PRODUCTION_REQUEST", allocationSize = 1)
    @Column(name = "PR_ID")
    private long prId;

    @Column(name = "MANAGEMENT_EMPLOYEE")
    private long managementEmployee;

    @Column(name = "PRODUCTION_EMPLOYEE")
    private long productionEmployee;

    @Column(name = "TO_COMPANY")
    private String toCompany;

    @Column(name = "TARGET_QTY", nullable = false)
    private int targetQty;

    @Column(name = "PLANNED_QTY")
    private int plannedQty;

    @Column(name = "COMPLETED_QTY")
    private int completedQty;

    @Column(name = "UNIT")
    private String unit;

    // ⭐ DateStringConverter 적용
    @Column(name = "REQUEST_DATE")
    @Convert(converter = DateStringConverter.class)
    private String requestDate;

    @Column(name = "PRODUCTION_START_DATE")
    @Convert(converter = DateStringConverter.class)
    private String productionStartDate;

    @Column(name = "DEADLINE")
    @Convert(converter = DateStringConverter.class)  // 추가
    private String deadline;

    @Column(name = "END_DATE")
    @Convert(converter = DateStringConverter.class)
    private String endDate;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "SERIAL_CODE")
    private String serialCode;

    // Getters and Setters는 그대로 유지
    public long getPrId() { return prId; }
    public void setPrId(long prId) { this.prId = prId; }

    public long getManagementEmployee() { return managementEmployee; }
    public void setManagementEmployee(long managementEmployee) { this.managementEmployee = managementEmployee; }

    public long getProductionEmployee() { return productionEmployee; }
    public void setProductionEmployee(long productionEmployee) { this.productionEmployee = productionEmployee; }

    public String getToCompany() { return toCompany; }
    public void setToCompany(String toCompany) { this.toCompany = toCompany; }

    public int getTargetQty() { return targetQty; }
    public void setTargetQty(int targetQty) { this.targetQty = targetQty; }

    public int getPlannedQty() { return plannedQty; }
    public void setPlannedQty(int plannedQty) { this.plannedQty = plannedQty; }

    public int getCompletedQty() { return completedQty; }
    public void setCompletedQty(int completedQty) { this.completedQty = completedQty; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

    public String getProductionStartDate() { return productionStartDate; }
    public void setProductionStartDate(String productionStartDate) { this.productionStartDate = productionStartDate; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getSerialCode() { return serialCode; }
    public void setSerialCode(String serialCode) { this.serialCode = serialCode; }

    public int getCompleteStatus() {
        if (endDate != null) return 2;
        if (productionStartDate != null) return 1;
        return 0;
    }

    // Builder는 그대로 유지
    public static class Builder {
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
        private String serialCode;

        public Builder prId(long prId) {
            this.prId = prId;
            return this;
        }

        public Builder managementEmployee(long managementEmployee) {
            this.managementEmployee = managementEmployee;
            return this;
        }

        public Builder productionEmployee(long productionEmployee) {
            this.productionEmployee = productionEmployee;
            return this;
        }

        public Builder toCompany(String toCompany) {
            this.toCompany = toCompany;
            return this;
        }

        public Builder targetQty(int targetQty) {
            this.targetQty = targetQty;
            return this;
        }

        public Builder plannedQty(int plannedQty) {
            this.plannedQty = plannedQty;
            return this;
        }

        public Builder completedQty(int completedQty) {
            this.completedQty = completedQty;
            return this;
        }

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder requestDate(String requestDate) {
            this.requestDate = requestDate;
            return this;
        }

        public Builder productionStartDate(String productionStartDate) {
            this.productionStartDate = productionStartDate;
            return this;
        }

        public Builder deadline(String deadline) {
            this.deadline = deadline;
            return this;
        }

        public Builder endDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public Builder serialCode(String serialCode) {
            this.serialCode = serialCode;
            return this;
        }

        public ProductionRequestEntity build() {
            ProductionRequestEntity entity = new ProductionRequestEntity();
            entity.prId = this.prId;
            entity.managementEmployee = this.managementEmployee;
            entity.productionEmployee = this.productionEmployee;
            entity.toCompany = this.toCompany;
            entity.targetQty = this.targetQty;
            entity.plannedQty = this.plannedQty;
            entity.completedQty = this.completedQty;
            entity.unit = this.unit;
            entity.requestDate = this.requestDate;
            entity.productionStartDate = this.productionStartDate;
            entity.deadline = this.deadline;
            entity.endDate = this.endDate;
            entity.note = this.note;
            entity.serialCode = this.serialCode;
            return entity;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}