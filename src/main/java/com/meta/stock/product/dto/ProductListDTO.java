package com.meta.stock.product.dto;

public class ProductListDTO {
    private long productId;
    private String productName;

    public ProductListDTO() {}

    public ProductListDTO(long productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

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

    public static class Builder {
        private long productId;
        private String productName;

        public Builder productId(long productId) {
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