package com.meta.stock.materials.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MATERIALS")
public class MaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "material_gen")
    @SequenceGenerator(
            name = "material_gen",
            sequenceName = "SEQ_MATERIALS",
            allocationSize = 1
    )
    @Column(name = "MATERIALS_ID")
    private long materialsId;

    @Column(name = "MATERIAL_NAME")
    private String materialName;

    @Column(name = "LOTS_ID")
    private long lotsId;

    @Column(name = "LOSS")
    private float loss;

    // Getter
    public long getMaterialsId() {
        return materialsId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public long getLotsId() {
        return lotsId;
    }

    public Float getLoss() {
        return loss;
    }

    // Setter
    public void setMaterialsId(long materialsId) {
        this.materialsId = materialsId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setLotsId(long lotsId) {
        this.lotsId = lotsId;
    }

    public void setLoss(Float loss) {
        this.loss = loss;
    }
}