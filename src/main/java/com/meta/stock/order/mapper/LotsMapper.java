package com.meta.stock.order.mapper;

import com.meta.stock.order.dto.LotStockDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LotsMapper {
    List<LotStockDto> findLotsByProductIdOrderByExpiry(Long productId);

    void updateLotQty(long lotId, int newQty);

    void deleteZeroLot(long lotId);
}
