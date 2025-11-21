package com.meta.stock.materials.entity;

import com.meta.stock.product.entity.FixedProductEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "FIXED_MATERIAL")
public class FixedMaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fm_seq")
    @SequenceGenerator(name = "fm_seq", sequenceName = "FM_SEQ", allocationSize = 1)
    @Column(name = "fm_id")
    private long fmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fp_id", nullable = false)
    private FixedProductEntity fixedProduct;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "qty")
    private int qty;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "life_time")
    private int lifeTime;

    public long getFmId() { return fmId; }
    public FixedProductEntity getFixedProduct() { return fixedProduct; }
    public String getMaterialName() { return name; }
    public int getQty() { return qty; }
    public String getUnit() { return unit; }
    public String getName() {return name;}
    public int getLifeTime() {return lifeTime;}

    public void setFmId(long fmId) { this.fmId = fmId; }
    public void setFixedProduct(FixedProductEntity fixedProduct) { this.fixedProduct = fixedProduct; }
    public void setMaterialName(String name) { this.name = name; }
    public void setQty(int qty) { this.qty = qty; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setName(String name) {this.name = name;}
    public void setLifeTime(int lifeTime) {this.lifeTime = lifeTime;}
}