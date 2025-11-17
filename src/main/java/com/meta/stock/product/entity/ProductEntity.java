package com.meta.stock.product.entity;

import jakarta.persistence.*;

// 생산된 제품
@Entity
@Table(name="Products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    @SequenceGenerator(
            name = "product_gen",
            sequenceName = "PRODUCT_SEQ",
            allocationSize = 1
    )
    private long productId;
    // 제품 이름
    private String productName;
    // 제품의 로트 번호 - 생산된 수량, 유통기한
    private long lotsId;
}