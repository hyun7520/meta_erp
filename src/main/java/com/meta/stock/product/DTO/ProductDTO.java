package com.meta.stock.product.DTO;

public class ProductDTO {

    // Request용 DTO (생성/수정 시 사용)
    public static class Request {
        private String productName;    // 완제품명
        private int productionLoss;    // 생산 손실률
        private long lotsId;           // 로트 ID

        public Request() {}

        public Request(String productName, int productionLoss, long lotsId) {
            this.productName = productName;
            this.productionLoss = productionLoss;
            this.lotsId = lotsId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getProductionLoss() {
            return productionLoss;
        }

        public void setProductionLoss(int productionLoss) {
            this.productionLoss = productionLoss;
        }

        public long getLotsId() {
            return lotsId;
        }

        public void setLotsId(long lotsId) {
            this.lotsId = lotsId;
        }

        // Builder
        public static class RequestBuilder {
            private String productName;
            private int productionLoss;
            private long lotsId;

            public RequestBuilder productName(String productName) {
                this.productName = productName;
                return this;
            }

            public RequestBuilder productionLoss(int productionLoss) {
                this.productionLoss = productionLoss;
                return this;
            }

            public RequestBuilder lotsId(long lotsId) {
                this.lotsId = lotsId;
                return this;
            }

            public Request build() {
                return new Request(productName, productionLoss, lotsId);
            }
        }

        public static RequestBuilder builder() {
            return new RequestBuilder();
        }
    }

    // Response용 DTO
    public static class Response {
        private long productId;
        private String productName;
        private int productionLoss;
        private long prId;
        private long lotsId;

        public Response() {}

        public Response(long productId, String productName, int productionLoss, long prId, long lotsId) {
            this.productId = productId;
            this.productName = productName;
            this.productionLoss = productionLoss;
            this.prId = prId;
            this.lotsId = lotsId;
        }

        // Getters and Setters
        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getProductionLoss() {
            return productionLoss;
        }

        public void setProductionLoss(int productionLoss) {
            this.productionLoss = productionLoss;
        }

        public long getPrId() {
            return prId;
        }

        public void setPrId(long prId) {
            this.prId = prId;
        }

        public long getLotsId() {
            return lotsId;
        }

        public void setLotsId(long lotsId) {
            this.lotsId = lotsId;
        }

        // Builder
        public static class ResponseBuilder {
            private long productId;
            private String productName;
            private int productionLoss;
            private long prId;
            private long lotsId;

            public ResponseBuilder productId(long productId) {
                this.productId = productId;
                return this;
            }

            public ResponseBuilder productName(String productName) {
                this.productName = productName;
                return this;
            }

            public ResponseBuilder productionLoss(int productionLoss) {
                this.productionLoss = productionLoss;
                return this;
            }

            public ResponseBuilder prId(long prId) {
                this.prId = prId;
                return this;
            }

            public ResponseBuilder lotsId(long lotsId) {
                this.lotsId = lotsId;
                return this;
            }

            public Response build() {
                return new Response(productId, productName, productionLoss, prId, lotsId);
            }
        }

        public static ResponseBuilder builder() {
            return new ResponseBuilder();
        }
    }
}