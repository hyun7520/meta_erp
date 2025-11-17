package com.meta.stock.product.entity;

import jakarta.persistence.Entity;

@Entity
public class Fixed_Product {

    private int fpId;
    private String serialCode;
    private String name;
    // 단위단 생산시간
    private int productionTime;
}
