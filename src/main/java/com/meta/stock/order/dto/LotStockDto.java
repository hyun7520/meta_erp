package com.meta.stock.order.dto;

public class LotStockDto {

    private long lotId;
    private int qty;

    public long getLotId() { return lotId; }
    public int getQty() { return qty; }

    public void setLotId(long lotId) { this.lotId = lotId; }
    public void setQty(int qty) { this.qty = qty; }
}
