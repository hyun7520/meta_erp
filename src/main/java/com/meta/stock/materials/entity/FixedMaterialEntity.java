package com.meta.stock.materials.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "FixedMaterial")
public class FixedMaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fixed_material_gen")
    @SequenceGenerator(
            name = "fixed_material_gen",
            sequenceName = "FM_SEQ",
            allocationSize = 1
    )
    private int fmId;
    // 재료가 사용되는 제품의 아이디 - 외래키
    private int fpId;
    private String materialName;
    // 단위 제품을 만드는데 필요한 수량
    private int qty;

    public void setFmId(int fmId) {
        this.fmId = fmId;
    }

    public void setFpId(int fpId) {
        this.fpId = fpId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getFmId() {
        return fmId;
    }

    public int getFpId() {
        return fpId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public int getQty() {
        return qty;
    }
}
