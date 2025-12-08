package com.meta.stock.product.dto;

public class ProductListDTO {
    private String serialCode;
    private String productName;

    public ProductListDTO() {}

    public ProductListDTO(String serialCode, String productName) {
        this.serialCode = serialCode;
        this.productName = productName;
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

    public static class Builder {
        private String productId;
        private String productName;

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public ProductListDTO build() {
            return new ProductListDTO(productId, productName);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}