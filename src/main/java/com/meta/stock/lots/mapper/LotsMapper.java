package com.meta.stock.lots.mapper;

import com.meta.stock.lots.dto.LotStockDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface LotsMapper {

    // 특정 제품의 로트를 유통기한 빠른 순으로 조회
    List<LotStockDto> findLotsBySerialCodeOrderByExpiry(String serialCode);

    // 로트 수량 업데이트
    void updateLotQty(@Param("lotId") Long lotId, @Param("qty") Integer qty);

    // 로트 삭제
    void deleteLotById(Long lotId);

    long storeProduct(Integer qty, int lifeTime);

    Long getLatestLot();

    List<LotStockDto> findLotsByFmIdByExpiry(Long fmId);
}
