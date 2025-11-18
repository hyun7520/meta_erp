package com.meta.stock.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "FixedProduct")
public class FixedProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fixed_product_gen")
    @SequenceGenerator(
            name = "fixed_product_gen",
            sequenceName = "FP_SEQ",
            allocationSize = 1
    )
    private long fpId;
    // 제품의 시리얼 코드
    private String serialCode;
    // 제품 이름
    private String name;
    // 생산시간
    private int productionTime;

    public long getFpId() {
        return fpId;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public String getName() {
        return name;
    }

    public int getProductionTime() {
        return productionTime;
    }

    public void setFpId(long fpId) {
        this.fpId = fpId;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductionTime(int productionTime) {
        this.productionTime = productionTime;
    }
}
