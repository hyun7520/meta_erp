package com.meta.stock.product.service;

import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.materials.mapper.MaterialMapper;
import com.meta.stock.product.dto.FixedProductDto;
import com.meta.stock.product.dto.ProductDTO;
import com.meta.stock.product.dto.ProductStockDto;
import com.meta.stock.product.entity.ProductEntity;
import com.meta.stock.product.repository.ProductRepository;
import com.meta.stock.product.mapper.ProductMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private ProductRepository productRepository;

    public List<ProductStockDto> getAllProductsStock() {
        return productMapper.findTotalProductStock();
    }

    public List<FixedProductDto> getFixedProductWithStockQty() {
        return productMapper.getFixedProductWithStockQty();
    }

    public Page<ProductStockDto> findTotalProductStock(String keyword, Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().next().getProperty();
        String sortDir = pageable.getSort().iterator().next().getDirection().name();

        List<ProductStockDto> content = productMapper.findProductStockWithPaging(
                keyword, sortBy, sortDir, offset, limit
        );

        long total = productMapper.countProductStock(keyword);

        return new PageImpl<>(content, pageable, total);
    }

    public int getCurrentProductStock(String serialCode) {
        return productMapper.getCurrentProductStock(serialCode);
    }

    public List<MaterialRequirementDto> calculateMaterialRequirements(String serialCode, int productQty) {
        // Fixed_Material에서 BOM 정보 조회
        List<MaterialRequirementDto> requirements = productMapper.getMaterialRequirements(serialCode);

        // 필요 수량 계산 및 현재 재고 조회
        for (MaterialRequirementDto req : requirements) {
            int totalRequired = req.getRequiredQty() * productQty;
            req.setRequiredQty(totalRequired);
            // 현재 재료 재고 조회
            int currentStock = materialMapper.getCurrentStock(req.getMaterialName());
            req.setCurrentStock(currentStock);
        }

        return requirements;
    }

    public List<MaterialDto> getRequiredMaterials(Long fpId) {
        return materialMapper.getRequiredMaterials(fpId);
    }

    public int getProductionLoss(Long fpId) {
        return productMapper.getProductionLoss(fpId);
    }

    // 전체 제품 목록 조회
    @Transactional(readOnly = true)
    public List<ProductDTO.Response> getAllProducts() {
        List<ProductEntity> entities = productRepository.findAllByOrderByProductIdAsc();
        return entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Entity -> Response DTO 변환
    private ProductDTO.Response convertToResponse(ProductEntity entity) {
        return ProductDTO.Response.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .productionLoss(entity.getProductionLoss())
                .prId(entity.getPrId())
                .lotsId(entity.getLotsId())
                .build();
    }
}
