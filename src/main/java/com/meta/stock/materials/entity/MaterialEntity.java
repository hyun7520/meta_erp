package com.meta.stock.materials.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class MaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "material_gen")
    @SequenceGenerator(
            name = "material_gen",
            sequenceName = "MATERIAL_SEQ",
            allocationSize = 1
    )
    private int materialId;
    // 재료 이름
    private String materialName;
    // 현 재료의 입고일자, 사용기한을 저장하는 테이블 lots -> 외래키
    private int lotsId;
    // 재료별 로스율
    private float loss;

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setLotsId(int lotsId) {
        this.lotsId = lotsId;
    }

    public void setLoss(float loss) {
        this.loss = loss;
    }

    public int getMaterialId() {
        return materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public int getLotsId() {
        return lotsId;
    }

    public float getLoss() {
        return loss;
    }
}
