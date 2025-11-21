package com.meta.stock.product.entity;

import com.meta.stock.materials.entity.FixedMaterialEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FIXED_PRODUCT")
public class FixedProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fp_seq")
    @SequenceGenerator(name = "fp_seq", sequenceName = "FP_SEQ", allocationSize = 1)
    @Column(name = "fp_id")
    private long fpId;

    @Column(name = "serial_code", length = 255)
    private String serialCode;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "life_time")
    private int lifeTime;   // 유효기간(일)

    @OneToMany(mappedBy = "fixedProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FixedMaterialEntity> materials = new ArrayList<>();

    // getter & setter
    public long getFpId() { return fpId; }
    public String getSerialCode() { return serialCode; }
    public String getName() { return name; }
    public int getLifeTime() { return lifeTime; }

    public void setFpId(long fpId) { this.fpId = fpId; }
    public void setSerialCode(String serialCode) { this.serialCode = serialCode; }
    public void setName(String name) { this.name = name; }
    public void setProductionTime(int lifeTime) { this.lifeTime = this.lifeTime; }
}