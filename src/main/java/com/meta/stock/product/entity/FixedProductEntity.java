package com.meta.stock.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "FIXED_PRODUCT")
public class FixedProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fixed_product_gen")
    @SequenceGenerator(name = "fixed_product_gen", sequenceName = "FP_SEQ", allocationSize = 1)
    @Column(name = "FP_ID")
    private long fpId;

    @Column(name = "SERIAL_CODE")
    private String serialCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRODUCTION_TIME")
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
