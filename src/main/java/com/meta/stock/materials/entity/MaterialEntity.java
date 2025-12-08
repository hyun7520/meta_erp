package com.meta.stock.materials.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MATERIALS")
public class MaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materials_seq")
    @SequenceGenerator(name = "materials_seq", sequenceName = "materials_seq", allocationSize = 1)
    @Column(name = "materials_id")
    private Long materialsId;

    @Column(name = "material_name", length = 50)
    private String materialName;

    @Column(name = "lots_id")
    private Long lotsId;

    @Column(name = "mr_id")
    private Long mrId;

    @Column(name = "MATERIAL_LOSS")
    private int materialLoss;

    public long getMaterialsId() { return materialsId; }
    public String getMaterialName() { return materialName; }
    public long getLotsId() { return lotsId; }
    public Long getMrId() { return mrId; }
    public int getMaterialLoss() { return materialLoss; }

    public void setMaterialsId(Long materialsId) { this.materialsId = materialsId; }

    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public void setLotsId(Long lotsId) {
        this.lotsId = lotsId;
    }

    public void setMrId(Long mrId) {
        this.mrId = mrId;
    }

    public void setMaterialLoss(int materialLoss) {
        this.materialLoss = materialLoss;
    }
}
