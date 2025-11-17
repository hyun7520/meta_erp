package com.meta.stock.materials.entity;

import jakarta.persistence.*;

@Entity
public class FixedMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fixed_material_gen")
    @SequenceGenerator(
            name = "fixed_material_gen",
            sequenceName = "FM_SEQ",
            allocationSize = 1
    )
    private int fmId;
    // 재료가 사용되는 제품의 아이디 - 외래키
    private int fpId;
    private String materialName;
    // 단위 제품을 만드는데 필요한 수량
    private int qty;
}
