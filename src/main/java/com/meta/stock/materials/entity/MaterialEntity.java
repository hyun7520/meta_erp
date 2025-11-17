package com.meta.stock.materials.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class MaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "material_gen")
    @SequenceGenerator(
            name = "material_gen",
            sequenceName = "MATERIAL_SEQ",
            allocationSize = 1
    )
    private int material_id;
    // 재료 이름
    private String material_name;
    // 현 재료의 입고일자, 사용기한을 저장하는 테이블 lots -> 외래키
    private int lots_id;
    // 재료별 로스율
    private float loss;
}
