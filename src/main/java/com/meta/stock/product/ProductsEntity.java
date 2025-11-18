package com.meta.stock.product;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTS")
public class ProductsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prod_seq")
    @SequenceGenerator(name = "prod_seq", sequenceName = "SEQ_PRODUCTS", allocationSize = 1)
    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "LOTS_ID")
    private long lotsId;

    // Getter
    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public long getLotsId() {
        return lotsId;
    }

    // Setter
    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setLotsId(long lotsId) {
        this.lotsId = lotsId;
    }
}