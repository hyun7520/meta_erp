package com.meta.stock.product.entity;

import jakarta.persistence.*;

@Entity
public class FixedProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fixed_product_gen")
    @SequenceGenerator(
            name = "fixed_product_gen",
            sequenceName = "FP_SEQ",
            allocationSize = 1
    )
    private int fpId;
    // 제품의 시리얼 코드
    private String serialCode;
    // 제품 이름
    private String name;
    // 생산시간
    private int productionTime;
}
