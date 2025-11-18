package com.meta.stock.product;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTS")
public class ProductsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prod_seq")
    @SequenceGenerator(name = "prod_seq", sequenceName = "SEQ_PRODUCTS", allocationSize = 1)
    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "LOTS_ID")
    private Long lotsId;

    // Getter
    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getLotsId() {
        return lotsId;
    }

    // Setter
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setLotsId(Long lotsId) {
        this.lotsId = lotsId;
    }
}