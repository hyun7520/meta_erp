package com.meta.stock.product.entity;

import jakarta.persistence.*;

import java.util.Date;

// 재료 요청서 
@Entity
public class MaterialRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "material_request_gen")
    @SequenceGenerator(
            name = "material_request_gen",
            sequenceName = "MR_SEQ",
            allocationSize = 1
    )
    private int mr_id;
    // 요청한 재료, Material 외래키
    private int materialId;
    // 요청 담당장, Employee 외래키
    private int requestBy;
    // 요청일자
    private Date requestDate;
    // 요청 수량
    private int qty;
    // 요청 단위
    private String unit;
    // 요청 허가 여부, 0 또는 1로
    private int approved;
    // 요청 허가 일자
    private Date approvedDate;
    // 추가 사항 메모
    private String note;
}
