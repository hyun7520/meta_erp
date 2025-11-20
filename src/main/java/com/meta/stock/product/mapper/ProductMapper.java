package com.meta.stock.product.mapper;

import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.product.dto.ProductDto;
import com.meta.stock.product.dto.ProductStockDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<ProductStockDto> findProductStockWithPaging(String keyword, String sortBy, String sortDir, int offset, int limit);

    long countProductStock(String keyword);

    int getCurrentProductStock(String productId);

    void deleteProductByLotId(Long lotId);

    List<MaterialRequirementDto> getMaterialRequirements(String serialCode);

    void decreaseProductStock(String serialCode, int qty);
}
