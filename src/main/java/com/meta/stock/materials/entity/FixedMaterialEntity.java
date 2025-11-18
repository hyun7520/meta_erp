package com.meta.stock.materials.entity;

import com.meta.stock.product.entity.FixedProductEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "FIXED_MATERIAL")
public class FixedMaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fixed_material_gen")
    @SequenceGenerator(name = "fixed_material_gen", sequenceName = "FM_SEQ", allocationSize = 1)
    @Column(name = "FM_ID")
    private int fmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FP_ID", nullable = false)
    private FixedProductEntity fixedProduct;

    @Column(name = "MATERIAL_NAME")
    private String materialName;

    @Column(name = "QTY")
    private int qty;

    @Column(name = "UNIT", nullable = false, length = 20)
    private String unit;

    public int getFmId() { return fmId; }
    public void setFmId(int fmId) { this.fmId = fmId; }
    public FixedProductEntity getFixedProduct() { return fixedProduct; }
    public void setFixedProduct(FixedProductEntity fixedProduct) { this.fixedProduct = fixedProduct; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}