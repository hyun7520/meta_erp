package com.meta.stock.product.entity;

import com.meta.stock.materials.entity.FixedMaterialEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FIXED_PRODUCT")
public class FixedProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fixed_product_gen")
    @SequenceGenerator(name = "fixed_product_gen", sequenceName = "FP_SEQ", allocationSize = 1)
    @Column(name = "FP_ID")
    private long fpId;

    @Column(name = "SERIAL_CODE")
    private String serialCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRODUCTION_TIME")
    private int productionTime;

    // OneToMany 양방향 (자식에서 mappedBy로 연결)
    @OneToMany(mappedBy = "fixedProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FixedMaterialEntity> materials = new ArrayList<>();

    public void addMaterial(FixedMaterialEntity material) {
        materials.add(material);
        material.setFixedProduct(this);
    }

    public void removeMaterial(FixedMaterialEntity material) {
        materials.remove(material);
        material.setFixedProduct(null);
    }

    // getter & setter
    public long getFpId() { return fpId; }
    public List<FixedMaterialEntity> getMaterials() { return materials; }
    public String getSerialCode() { return serialCode; }
    public String getName() { return name; }
    public int getProductionTime() { return productionTime; }

    public void setFpId(int fpId) { this.fpId = fpId; }
    public void setMaterials(List<FixedMaterialEntity> materials) { this.materials = materials; }
    public void setSerialCode(String serialCode) { this.serialCode = serialCode; }
    public void setName(String name) { this.name = name; }
    public void setProductionTime(int productionTime) { this.productionTime = productionTime; }
}