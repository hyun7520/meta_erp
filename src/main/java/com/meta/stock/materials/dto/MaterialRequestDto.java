package com.meta.stock.materials.dto;

import java.time.LocalDate;
import java.util.Date;

public class MaterialRequestDto {

    private long mrId;
    private long fmId;
    private long requestBy;
    private String materialName;
    private int qty;
    private String requestDate = LocalDate.now().toString();
    private int approved;
    private String approvedDate;
    private String note;

    public long getMrId() {return mrId;}
    public long getFmId() {return fmId;}
    public long getRequestBy() {return requestBy;}
    public String getMaterialName() {return materialName;}
    public int getQty() {return qty;}
    public String getRequestDate() {return requestDate;}
    public int getApproved() {return approved;}
    public String getApprovedDate() {return approvedDate;}
    public String getNote() {return note;}

    public void setMrId(long mrId) {this.mrId = mrId;}
    public void setFmId(long fmId) {this.fmId = fmId;}
    public void setRequestBy(long requestBy) {this.requestBy = requestBy;}
    public void setMaterialName(String materialName) {this.materialName = materialName;}
    public void setQty(int qty) {this.qty = qty;}
    public void setRequestDate(String requestDate) {this.requestDate = requestDate;}
    public void setApproved(int approved) {this.approved = approved;}
    public void setApprovedDate(String approvedDate) {this.approvedDate = approvedDate;}
    public void setNote(String note) {this.note = note;}

    @Override
    public String toString() {
        return String.format("fmId: %d,재료: %s, 수량: %d, 담장자: %s, 비고: %s, 요청일자: %s",
                fmId, materialName, qty, requestBy, note, requestDate);
    }

    // Request용 DTO (생성/수정 시 사용)
    public static class Request {
        private long materialId;      // 원재료 ID
        private long requestBy;        // 요청자 직원 ID
        private int qty;               // 수량
        private String unit;           // 단위
        private String note;           // 비고

        public Request() {}

        public Request(long materialId, long requestBy, int qty, String unit, String note) {
            this.materialId = materialId;
            this.requestBy = requestBy;
            this.qty = qty;
            this.unit = unit;
            this.note = note;
        }

        public long getMaterialId() {
            return materialId;
        }

        public void setMaterialId(long materialId) {
            this.materialId = materialId;
        }

        public long getRequestBy() {
            return requestBy;
        }

        public void setRequestBy(long requestBy) {
            this.requestBy = requestBy;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        // Builder
        public static class RequestBuilder {
            private long materialId;
            private long requestBy;
            private int qty;
            private String unit;
            private String note;

            public RequestBuilder materialId(long materialId) {
                this.materialId = materialId;
                return this;
            }

            public RequestBuilder requestBy(long requestBy) {
                this.requestBy = requestBy;
                return this;
            }

            public RequestBuilder qty(int qty) {
                this.qty = qty;
                return this;
            }

            public RequestBuilder unit(String unit) {
                this.unit = unit;
                return this;
            }

            public RequestBuilder note(String note) {
                this.note = note;
                return this;
            }

            public Request build() {
                return new Request(materialId, requestBy, qty, unit, note);
            }
        }

        public static RequestBuilder builder() {
            return new RequestBuilder();
        }
    }

    // Response용 DTO
    public static class Response {
        private long mrId;
        private long materialId;
        private String materialName;
        private long requestBy;
        private String requesterName;
        private String requestDate;
        private int qty;
        private String unit;
        private int approved; // 0: 미승인, 1: 승인, -1: 반려
        private String approvedDate;
        private String managementEmployeeName;
        private String productionEmployeeName;
        private String note;

        public Response() {}

        public Response(long mrId, long materialId, String materialName, long requestBy, String requesterName,
                        String requestDate, int qty, String unit, int approved, String approvedDate,
                        String managementEmployeeName, String productionEmployeeName, String note) {
            this.mrId = mrId;
            this.materialId = materialId;
            this.materialName = materialName;
            this.requestBy = requestBy;
            this.requesterName = requesterName;
            this.requestDate = requestDate;
            this.qty = qty;
            this.unit = unit;
            this.approved = approved;
            this.approvedDate = approvedDate;
            this.managementEmployeeName = managementEmployeeName;
            this.productionEmployeeName = productionEmployeeName;
            this.note = note;
        }

        // Getters and Setters

        public long getMrId() { return mrId; }
        public void setMrId(long mrId) { this.mrId = mrId; }

        public long getMaterialId() { return materialId; }
        public void setMaterialId(long materialId) { this.materialId = materialId; }

        public String getMaterialName() { return materialName; }
        public void setMaterialName(String materialName) { this.materialName = materialName; }

        public long getRequestBy() { return requestBy; }
        public void setRequestBy(long requestBy) { this.requestBy = requestBy; }

        public String getRequesterName() { return requesterName; }
        public void setRequesterName(String requesterName) { this.requesterName = requesterName; }

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

        public String getManagementEmployeeName() { return managementEmployeeName; }
        public void setManagementEmployeeName(String managementEmployeeName) { this.managementEmployeeName = managementEmployeeName; }

        public String getProductionEmployeeName() { return productionEmployeeName; }
        public void setProductionEmployeeName(String productionEmployeeName) { this.productionEmployeeName = productionEmployeeName; }

        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }

        // Builder
        public static class ResponseBuilder {
            private long mrId;
            private long materialId;
            private String materialName;
            private long requestBy;
            private String requesterName;
            private String requestDate;
            private int qty;
            private String unit;
            private int approved;
            private String approvedDate;
            private String managementEmployeeName;
            private String productionEmployeeName;
            private String note;

            public ResponseBuilder mrId(long mrId) {
                this.mrId = mrId;
                return this;
            }

            public ResponseBuilder materialId(long materialId) {
                this.materialId = materialId;
                return this;
            }

            public ResponseBuilder materialName(String materialName) {
                this.materialName = materialName;
                return this;
            }

            public ResponseBuilder requestBy(long requestBy) {
                this.requestBy = requestBy;
                return this;
            }

            public ResponseBuilder requesterName(String requesterName) {
                this.requesterName = requesterName;
                return this;
            }

            public ResponseBuilder requestDate(String requestDate) {
                this.requestDate = requestDate;
                return this;
            }

            public ResponseBuilder qty(int qty) {
                this.qty = qty;
                return this;
            }

            public ResponseBuilder unit(String unit) {
                this.unit = unit;
                return this;
            }

            public ResponseBuilder approved(int approved) {
                this.approved = approved;
                return this;
            }

            public ResponseBuilder approvedDate(String approvedDate) {
                this.approvedDate = approvedDate;
                return this;
            }

            public ResponseBuilder managementEmployeeName(String managementEmployeeName) {
                this.managementEmployeeName = managementEmployeeName;
                return this;
            }

            public ResponseBuilder productionEmployeeName(String productionEmployeeName) {
                this.productionEmployeeName = productionEmployeeName;
                return this;
            }

            public ResponseBuilder note(String note) {
                this.note = note;
                return this;
            }

            public Response build() {
                return new Response(mrId, materialId, materialName, requestBy, requesterName, requestDate,
                        qty, unit, approved, approvedDate, managementEmployeeName, productionEmployeeName, note);
            }
        }

        public static ResponseBuilder builder() {
            return new ResponseBuilder();
        }
    }

    // Approval용 DTO
    public static class Approval {
        private long mrId;
        private long managementEmployee; // 경영 담당자 ID
        private long productionEmployee; // 생산 담당자 ID
        private int approved;    // 1: 승인, -1: 반려
        private String note;     // 사유

        public Approval() {}

        public Approval(long mrId, long managementEmployee, long productionEmployee, int approved, String note) {
            this.mrId = mrId;
            this.managementEmployee = managementEmployee;
            this.productionEmployee = productionEmployee;
            this.approved = approved;
            this.note = note;
        }

        // Getters and Setters

        public long getMrId() {
            return mrId;
        }

        public void setMrId(long mrId) {
            this.mrId = mrId;
        }

        public long getManagementEmployee() {
            return managementEmployee;
        }

        public void setManagementEmployee(long managementEmployee) {
            this.managementEmployee = managementEmployee;
        }

        public long getProductionEmployee() {
            return productionEmployee;
        }

        public void setProductionEmployee(long productionEmployee) {
            this.productionEmployee = productionEmployee;
        }

        public int getApproved() {
            return approved;
        }

        public void setApproved(int approved) {
            this.approved = approved;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        // Builder
        public static class ApprovalBuilder {
            private long mrId;
            private long managementEmployee;
            private long productionEmployee;
            private int approved;
            private String note;

            public ApprovalBuilder mrId(long mrId) {
                this.mrId = mrId;
                return this;
            }

            public ApprovalBuilder managementEmployee(long managementEmployee) {
                this.managementEmployee = managementEmployee;
                return this;
            }

            public ApprovalBuilder productionEmployee(long productionEmployee) {
                this.productionEmployee = productionEmployee;
                return this;
            }

            public ApprovalBuilder approved(int approved) {
                this.approved = approved;
                return this;
            }

            public ApprovalBuilder note(String note) {
                this.note = note;
                return this;
            }

            public Approval build() {
                return new Approval(mrId, managementEmployee, productionEmployee, approved, note);
            }
        }

        public static ApprovalBuilder builder() {
            return new ApprovalBuilder();
        }
    }
}
