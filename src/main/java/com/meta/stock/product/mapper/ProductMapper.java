package com.meta.stock.product.mapper;

import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.product.dto.FixedProductDto;
import com.meta.stock.product.dto.ProductStockDto;
import com.meta.stock.product.dto.ProductDemandBean;
import com.meta.stock.product.dto.ProductsAmountListBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    List<ProductsAmountListBean> getDashProductsList(Map<String, Object> param);
    List<ProductDemandBean> getDashProductDemand();

    int getTotalListSize(Map<String, Object> param);

    List<ProductStockDto> findTotalProductStock();

    List<ProductStockDto> findProductStockWithPaging(String keyword, String sortBy, String sortDir, int offset, int limit);

    long countProductStock(String keyword);

    int getCurrentProductStock(String productId);

    void deleteProductByLotId(Long lotId);

    List<MaterialRequirementDto> getMaterialRequirements(String serialCode);

    List<FixedProductDto> getFixedProductWithStockQty();

    int getProductionLoss(Long fpId);

    FixedProductDto getNameAndLifeTime(Long fpId);

    void produceProduct(String productName, int loss, Long prId, Long lotsId);
}
