package com.meta.stock.product.DTO;

public class ProductionRequestDTO {

    // Request용 DTO (생성 시 사용)
    public static class Request {
        private Long productId;        // HTML에서 전송
        private String serialCode;     // 또는 직접 serialCode 전송
        private String requestBy;
        private int qty;
        private String unit;
        private String requestDate;
        private String deadline;

        public Request() {}

        public Request(Long productId, String serialCode, String requestBy, int qty,
                       String unit, String requestDate, String deadline) {
            this.productId = productId;
            this.serialCode = serialCode;
            this.requestBy = requestBy;
            this.qty = qty;
            this.unit = unit;
            this.requestDate = requestDate;
            this.deadline = deadline;
        }

        // Getters and Setters
        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getSerialCode() {
            return serialCode;
        }

        public void setSerialCode(String serialCode) {
            this.serialCode = serialCode;
        }

        public String getRequestBy() {
            return requestBy;
        }

        public void setRequestBy(String requestBy) {
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

        public String getRequestDate() {
            return requestDate;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        // Builder 패턴
        public static class RequestBuilder {
            private Long productId;
            private String serialCode;
            private String requestBy;
            private int qty;
            private String unit;
            private String requestDate;
            private String deadline;

            public RequestBuilder productId(Long productId) {
                this.productId = productId;
                return this;
            }

            public RequestBuilder serialCode(String serialCode) {
                this.serialCode = serialCode;
                return this;
            }

            public RequestBuilder requestBy(String requestBy) {
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

            public RequestBuilder requestDate(String requestDate) {
                this.requestDate = requestDate;
                return this;
            }

            public RequestBuilder deadline(String deadline) {
                this.deadline = deadline;
                return this;
            }

            public Request build() {
                return new Request(productId, serialCode, requestBy, qty, unit, requestDate, deadline);
            }
        }

        public static RequestBuilder builder() {
            return new RequestBuilder();
        }
    }

    // Response용 DTO
    public static class Response {
        private long orderId;           // PR_ID
        private String serialCode;      // 고정제품 시리얼 코드
        private String productName;     // 완제품명
        private String requestBy;       // 고객사명 (toCompany)
        private int qty;                // 목표 수량 (targetQty)
        private String unit;            // 단위
        private String requestDate;     // 요청일
        private String deadline;        // 납기일
        private int complete;           // 0: 대기중, 1: 생산시작, 2: 생산완료
        private String productionStartDate; // 생산 시작일
        private String endDate;         // 생산 완료일

        public Response() {}

        public Response(long orderId, String serialCode, String productName, String requestBy,
                        int qty, String unit, String requestDate, String deadline, int complete,
                        String productionStartDate, String endDate) {
            this.orderId = orderId;
            this.serialCode = serialCode;
            this.productName = productName;
            this.requestBy = requestBy;
            this.qty = qty;
            this.unit = unit;
            this.requestDate = requestDate;
            this.deadline = deadline;
            this.complete = complete;
            this.productionStartDate = productionStartDate;
            this.endDate = endDate;
        }

        // Getters and Setters
        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public String getSerialCode() {
            return serialCode;
        }

        public void setSerialCode(String serialCode) {
            this.serialCode = serialCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getRequestBy() {
            return requestBy;
        }

        public void setRequestBy(String requestBy) {
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

        public String getRequestDate() {
            return requestDate;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public int getComplete() {
            return complete;
        }

        public void setComplete(int complete) {
            this.complete = complete;
        }

        public String getProductionStartDate() {
            return productionStartDate;
        }

        public void setProductionStartDate(String productionStartDate) {
            this.productionStartDate = productionStartDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        // Builder 패턴
        public static class ResponseBuilder {
            private long orderId;
            private String serialCode;
            private String productName;
            private String requestBy;
            private int qty;
            private String unit;
            private String requestDate;
            private String deadline;
            private int complete;
            private String productionStartDate;
            private String endDate;

            public ResponseBuilder orderId(long orderId) {
                this.orderId = orderId;
                return this;
            }

            public ResponseBuilder serialCode(String serialCode) {
                this.serialCode = serialCode;
                return this;
            }

            public ResponseBuilder productName(String productName) {
                this.productName = productName;
                return this;
            }

            public ResponseBuilder requestBy(String requestBy) {
                this.requestBy = requestBy;
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

            public ResponseBuilder requestDate(String requestDate) {
                this.requestDate = requestDate;
                return this;
            }

            public ResponseBuilder deadline(String deadline) {
                this.deadline = deadline;
                return this;
            }

            public ResponseBuilder complete(int complete) {
                this.complete = complete;
                return this;
            }

            public ResponseBuilder productionStartDate(String productionStartDate) {
                this.productionStartDate = productionStartDate;
                return this;
            }

            public ResponseBuilder endDate(String endDate) {
                this.endDate = endDate;
                return this;
            }

            public Response build() {
                return new Response(orderId, serialCode, productName, requestBy, qty, unit,
                        requestDate, deadline, complete, productionStartDate, endDate);
            }
        }

        public static ResponseBuilder builder() {
            return new ResponseBuilder();
        }
    }

    // Production Status Update용 DTO
    public static class UpdateStatus {
        private long prId;
        private int action; // 1: 생산시작, 2: 생산완료, 3: 생산 취소

        public UpdateStatus() {}

        public UpdateStatus(long prId, int action) {
            this.prId = prId;
            this.action = action;
        }

        public long getPrId() {
            return prId;
        }

        public void setPrId(long prId) {
            this.prId = prId;
        }

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        // Builder 패턴
        public static class UpdateStatusBuilder {
            private long prId;
            private int action;

            public UpdateStatusBuilder prId(long prId) {
                this.prId = prId;
                return this;
            }

            public UpdateStatusBuilder action(int action) {
                this.action = action;
                return this;
            }

            public UpdateStatus build() {
                return new UpdateStatus(prId, action);
            }
        }

        public static UpdateStatusBuilder builder() {
            return new UpdateStatusBuilder();
        }
    }

    // 인기 상품 DTO
    public static class TopProduct {
        private String productName;
        private int totalQty;
        private String unit;
        private int orderCount;

        public TopProduct() {}

        public TopProduct(String productName, int totalQty, String unit, int orderCount) {
            this.productName = productName;
            this.totalQty = totalQty;
            this.unit = unit;
            this.orderCount = orderCount;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getTotalQty() {
            return totalQty;
        }

        public void setTotalQty(int totalQty) {
            this.totalQty = totalQty;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getOrderCount() {
            return orderCount;
        }

        public void setOrderCount(int orderCount) {
            this.orderCount = orderCount;
        }

        // Builder 패턴
        public static class TopProductBuilder {
            private String productName;
            private int totalQty;
            private String unit;
            private int orderCount;

            public TopProductBuilder productName(String productName) {
                this.productName = productName;
                return this;
            }

            public TopProductBuilder totalQty(int totalQty) {
                this.totalQty = totalQty;
                return this;
            }

            public TopProductBuilder unit(String unit) {
                this.unit = unit;
                return this;
            }

            public TopProductBuilder orderCount(int orderCount) {
                this.orderCount = orderCount;
                return this;
            }

            public TopProduct build() {
                TopProduct topProduct = new TopProduct();
                topProduct.setProductName(this.productName);
                topProduct.setTotalQty(this.totalQty);
                topProduct.setUnit(this.unit);
                topProduct.setOrderCount(this.orderCount);
                return topProduct;
            }
        }

        public static TopProductBuilder builder() {
            return new TopProductBuilder();
        }
    }
}