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

    public long getMaterialsId() { return materialsId; }
    public String getMaterialName() { return materialName; }
    public long getLotsId() { return lotsId; }

    public void setMaterialId(long materialsId) { this.materialsId = materialsId; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public void setLotsId(long lotsId) { this.lotsId = lotsId; }
}
