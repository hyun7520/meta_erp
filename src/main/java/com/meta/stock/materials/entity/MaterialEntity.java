package com.meta.stock.materials.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Material")
public class MaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "material_gen")
    @SequenceGenerator(name = "material_gen", sequenceName = "MATERIAL_SEQ", allocationSize = 1)
    @Column(name = "MATERIAL_ID")
    private long materialId;

    @Column(name = "MATERIAL_NAME")
    private String materialName;

    @Column(name = "LOTS_ID")
    private long lotsId;

    @Column(name = "LOSS")
    private float loss;

    public long getMaterialId() {
        return materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public long getLotsId() {
        return lotsId;
    }

    public float getLoss() {
        return loss;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setLotsId(long lotsId) {
        this.lotsId = lotsId;
    }

    public void setLoss(float loss) {
        this.loss = loss;
    }
}
