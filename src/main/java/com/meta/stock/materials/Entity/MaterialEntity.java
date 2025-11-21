package com.meta.stock.materials.Entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "MATERIALS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "material_seq")
    @SequenceGenerator(name = "material_seq", sequenceName = "SEQ_MATERIALS", allocationSize = 1)
    @Column(name = "MATERIALS_ID")
    private long materialsId;

    @Column(name = "MATERIAL_NAME", length = 50)
    private String materialName;

    @Column(name = "MATERIAL_LOSS")
    private int materialLoss;

    @Column(name = "LOTS_ID")
    private long lotsId;

    @Column(name = "MR_ID")
    private long mrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MR_ID", referencedColumnName = "MR_ID", insertable = false, updatable = false)
    private MaterialRequestEntity materialRequest;

    public long getMaterialsId() {
        return materialsId;
    }

    public void setMaterialsId(long materialsId) {
        this.materialsId = materialsId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getMaterialLoss() {
        return materialLoss;
    }

    public void setMaterialLoss(int materialLoss) {
        this.materialLoss = materialLoss;
    }

    public long getLotsId() {
        return lotsId;
    }

    public void setLotsId(long lotsId) {
        this.lotsId = lotsId;
    }

    public long getMrId() {
        return mrId;
    }

    public void setMrId(long mrId) {
        this.mrId = mrId;
    }

    public MaterialRequestEntity getMaterialRequest() {
        return materialRequest;
    }

    public void setMaterialRequest(MaterialRequestEntity materialRequest) {
        this.materialRequest = materialRequest;
    }
}