package com.meta.stock.lots.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LOTS")
public class LotsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lot_seq")
    @SequenceGenerator(name = "lot_seq", sequenceName = "lot_seq", allocationSize = 1)
    @Column(name = "lot_id")
    private Long lotId;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "storage_date")
    private String storageDate;

    @Column(name = "shelf_life_days")
    private String shelfLifeDays;

    @Column(name = "note", length = 255)
    private String note;

    public long getLotId() {return lotId;}
    public int getQty() {return qty;}
    public String getUnit() {return unit;}
    public String getStorageDate() {return storageDate;}
    public String getShelfLifeDays() {return shelfLifeDays;}
    public String getNote() {return note;}

    public void setLotId(long lotId) {this.lotId = lotId;}
    public void setQty(int qty) {this.qty = qty;}
    public void setUnit(String unit) {this.unit = unit;}
    public void setStorageDate(String storageDate) {this.storageDate = storageDate;}
    public void setShelfLifeDays(String shelfLifeDays) {this.shelfLifeDays = shelfLifeDays;}
    public void setNote(String note) {this.note = note;}
}
