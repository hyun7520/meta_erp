package com.meta.stock.product.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "SEQ_PRODUCTS", allocationSize = 1)
    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "PRODUCT_NAME", length = 50)
    private String productName;

    @Column(name = "PRODUCTION_LOSS")
    private Integer productionLoss;

    @Column(name = "PR_ID")
    private Long prId;

    @Column(name = "LOTS_ID")
    private Long lotsId;

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductionLoss() {
        return productionLoss;
    }

    public Long getPrId() {
        return prId;
    }

    public Long getLotsId() {
        return lotsId;
    }


    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductionLoss(Integer productionLoss) {
        this.productionLoss = productionLoss;
    }

    public void setPrId(Long prId) {
        this.prId = prId;
    }

    public void setLotsId(Long lotsId) {
        this.lotsId = lotsId;
    }
}