package com.meta.stock.order.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LOTS")
public class LotsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lots_gen")
    @SequenceGenerator(name = "lots_gen", sequenceName = "LOTS_SEQ", allocationSize = 1)
    private long lotId;
    @Column(name = "QTY")
    private int qty;
    @Column(name = "UNIT")
    private String unit;
    @Column(name = "STORAGE_DATE")
    private String storageDate;
    @Column(name = "SHELF_LIFE_DAYS")
    private String shelfLifeDays;
    @Column(name = "NOTE")
    private String note;
}
