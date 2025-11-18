package com.meta.stock.materials;

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
    private Integer materialsId;

    @Column(name = "MATERIAL_NAME")
    private String materialName;

    @Column(name = "LOTS_ID")
    private Integer lotsId;

    @Column(name = "LOSS")
    private Float loss;

    // Getter
    public Integer getMaterialsId() {
        return materialsId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public Integer getLotsId() {
        return lotsId;
    }

    public Float getLoss() {
        return loss;
    }

    // Setter
    public void setMaterialsId(Integer materialsId) {
        this.materialsId = materialsId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setLotsId(Integer lotsId) {
        this.lotsId = lotsId;
    }

    public void setLoss(Float loss) {
        this.loss = loss;
    }
}