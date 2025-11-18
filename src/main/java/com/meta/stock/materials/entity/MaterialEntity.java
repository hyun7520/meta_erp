package com.meta.stock.materials.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MATERIAL")
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

    // getter & setter
    public long getMaterialId() { return materialId; }
    public String getMaterialName() { return materialName; }
    public long getLotsId() { return lotsId; }
    public float getLoss() { return loss; }

    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public void setLotsId(int lotsId) { this.lotsId = lotsId; }
    public void setLoss(float loss) { this.loss = loss; }
}
