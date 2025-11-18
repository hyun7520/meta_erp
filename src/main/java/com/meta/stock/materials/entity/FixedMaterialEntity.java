package com.meta.stock.materials.entity;

import com.meta.stock.product.entity.FixedProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "FIXED_MATERIAL")
public class FixedMaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fixed_material_gen")
    @SequenceGenerator(name = "fixed_material_gen", sequenceName = "FM_SEQ", allocationSize = 1)
    @Column(name = "FM_ID")
    private long fmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FP_ID", nullable = false)
    private FixedProductEntity fixedProduct;

    @Column(name = "MATERIAL_NAME")
    private String materialName;

    @Column(name = "QTY")
    private int qty;

    @Column(name = "UNIT")
    private String unit;

    public long getFmId() {
        return fmId;
    }

    public FixedProductEntity getFixedProduct() {
        return fixedProduct;
    }

    public String getMaterialName() {
        return materialName;
    }

    public int getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setFmId(long fmId) {
        this.fmId = fmId;
    }

    public void setFixedProduct(FixedProductEntity fixedProduct) {
        this.fixedProduct = fixedProduct;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
