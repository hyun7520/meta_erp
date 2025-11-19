package com.meta.stock.order.entity;

import com.meta.stock.materials.entity.MaterialEntity;
import com.meta.stock.product.entity.ProductEntity;
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
