package com.meta.stock.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Lots")
public class LotsEntity {
    @Id
    private int lotId;
    private int qty;
    private String unit;
    private String storageDate;
    private String shelfLifeDays;
    private String note;

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setStorageDate(String storageDate) {
        this.storageDate = storageDate;
    }

    public void setShelfLifeDays(String shelfLifeDays) {
        this.shelfLifeDays = shelfLifeDays;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getLotId() {
        return lotId;
    }

    public int getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public String getStorageDate() {
        return storageDate;
    }

    public String getShelfLifeDays() {
        return shelfLifeDays;
    }

    public String getNote() {
        return note;
    }
}
