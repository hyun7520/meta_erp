package com.meta.stock.product.entity;

import jakarta.persistence.Entity;

import java.util.Date;

@Entity
public class MaterialRequest {

    private int mr_id;
    private int materialId;
    private int requestBy;
    private Date requestDate;
    private int qty;
    private String unit;
    private boolean approved;
    private Date approvedDate;
    private String note;
}
