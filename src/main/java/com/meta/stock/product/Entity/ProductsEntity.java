package com.meta.stock.product.Entity;

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

    // Production_Request 테이블에 PRODUCT_ID가 없으므로 양방향 관계 제거

    // Getters and Setters (Lombok @Getter 대신 수동으로 구현)

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductionLoss() {
        return productionLoss;
    }

    public void setProductionLoss(Integer productionLoss) {
        this.productionLoss = productionLoss;
    }

    public Long getPrId() {
        return prId;
    }

    public void setPrId(Long prId) {
        this.prId = prId;
    }

    public Long getLotsId() {
        return lotsId;
    }

    public void setLotsId(Long lotsId) {
        this.lotsId = lotsId;
    }
}