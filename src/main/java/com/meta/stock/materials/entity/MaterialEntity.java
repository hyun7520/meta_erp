package com.meta.stock.materials.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MATERIAL")
public class MaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "material_gen")
    @SequenceGenerator(name = "material_gen", sequenceName = "MATERIAL_SEQ", allocationSize = 1)
    @Column(name = "MATERIAL_ID")
    private int materialId;

    @Column(name = "MATERIAL_NAME")
    private String materialName;

    @Column(name = "LOTS_ID")
    private int lotsId;

    @Column(name = "LOSS")
    private float loss;

    // getter & setter
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public int getLotsId() { return lotsId; }
    public void setLotsId(int lotsId) { this.lotsId = lotsId; }
    public float getLoss() { return loss; }
    public void setLoss(float loss) { this.loss = loss; }
}
