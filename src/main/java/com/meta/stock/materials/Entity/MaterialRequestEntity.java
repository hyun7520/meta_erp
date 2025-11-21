package com.meta.stock.materials.Entity;

import com.meta.stock.user.Entity.EmployeeEntity;
import jakarta.persistence.*;

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

    // 요청자 엔티티 (DB에는 없지만 서비스 로직을 위해 추가)
    @Transient
    private EmployeeEntity requester;

    // 경영 담당자 엔티티 (DB에는 없지만 서비스 로직을 위해 추가)
    @Transient
    private EmployeeEntity managementEmployeeEntity;

    // 생산 담당자 엔티티 (DB에는 없지만 서비스 로직을 위해 추가)
    @Transient
    private EmployeeEntity productionEmployeeEntity;


    // Getters and Setters
    public long getMrId() { return mrId; }
    public void setMrId(long mrId) { this.mrId = mrId; }

    public long getRequestBy() { return requestBy; }
    public void setRequestBy(long requestBy) { this.requestBy = requestBy; }

    public long getManagementEmployee() { return managementEmployee; }
    public void setManagementEmployee(long managementEmployee) { this.managementEmployee = managementEmployee; }

    public long getProductionEmployee() { return productionEmployee; }
    public void setProductionEmployee(long productionEmployee) { this.productionEmployee = productionEmployee; }

    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public int getApproved() { return approved; }
    public void setApproved(int approved) { this.approved = approved; }

    public String getApprovedDate() { return approvedDate; }
    public void setApprovedDate(String approvedDate) { this.approvedDate = approvedDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public EmployeeEntity getRequester() { return requester; }
    public void setRequester(EmployeeEntity requester) { this.requester = requester; }

    public EmployeeEntity getManagementEmployeeEntity() { return managementEmployeeEntity; }
    public void setManagementEmployeeEntity(EmployeeEntity managementEmployeeEntity) { this.managementEmployeeEntity = managementEmployeeEntity; }

    public EmployeeEntity getProductionEmployeeEntity() { return productionEmployeeEntity; }
    public void setProductionEmployeeEntity(EmployeeEntity productionEmployeeEntity) { this.productionEmployeeEntity = productionEmployeeEntity; }

    // Builder
    public static class MaterialRequestEntityBuilder {
        private long mrId;
        private long requestBy;
        private long managementEmployee;
        private long productionEmployee;
        private String requestDate;
        private int qty;
        private String unit;
        private int approved;
        private String approvedDate;
        private String note;
        private EmployeeEntity requester;
        private EmployeeEntity managementEmployeeEntity;
        private EmployeeEntity productionEmployeeEntity;

        public MaterialRequestEntityBuilder mrId(long mrId) {
            this.mrId = mrId;
            return this;
        }

        public MaterialRequestEntityBuilder requestBy(long requestBy) {
            this.requestBy = requestBy;
            return this;
        }

        public MaterialRequestEntityBuilder managementEmployee(long managementEmployee) {
            this.managementEmployee = managementEmployee;
            return this;
        }

        public MaterialRequestEntityBuilder productionEmployee(long productionEmployee) {
            this.productionEmployee = productionEmployee;
            return this;
        }

        public MaterialRequestEntityBuilder requestDate(String requestDate) {
            this.requestDate = requestDate;
            return this;
        }

        public MaterialRequestEntityBuilder qty(int qty) {
            this.qty = qty;
            return this;
        }

        public MaterialRequestEntityBuilder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public MaterialRequestEntityBuilder approved(int approved) {
            this.approved = approved;
            return this;
        }

        public MaterialRequestEntityBuilder approvedDate(String approvedDate) {
            this.approvedDate = approvedDate;
            return this;
        }

        public MaterialRequestEntityBuilder note(String note) {
            this.note = note;
            return this;
        }

        public MaterialRequestEntityBuilder requester(EmployeeEntity requester) {
            this.requester = requester;
            return this;
        }

        public MaterialRequestEntityBuilder managementEmployeeEntity(EmployeeEntity managementEmployeeEntity) {
            this.managementEmployeeEntity = managementEmployeeEntity;
            return this;
        }

        public MaterialRequestEntityBuilder productionEmployeeEntity(EmployeeEntity productionEmployeeEntity) {
            this.productionEmployeeEntity = productionEmployeeEntity;
            return this;
        }

        public MaterialRequestEntity build() {
            MaterialRequestEntity entity = new MaterialRequestEntity();
            entity.setMrId(this.mrId);
            entity.setRequestBy(this.requestBy);
            entity.setManagementEmployee(this.managementEmployee);
            entity.setProductionEmployee(this.productionEmployee);
            entity.setRequestDate(this.requestDate);
            entity.setQty(this.qty);
            entity.setUnit(this.unit);
            entity.setApproved(this.approved);
            entity.setApprovedDate(this.approvedDate);
            entity.setNote(this.note);
            entity.setRequester(this.requester);
            entity.setManagementEmployeeEntity(this.managementEmployeeEntity);
            entity.setProductionEmployeeEntity(this.productionEmployeeEntity);
            return entity;
        }
    }

    public static MaterialRequestEntityBuilder builder() {
        return new MaterialRequestEntityBuilder();
    }
}