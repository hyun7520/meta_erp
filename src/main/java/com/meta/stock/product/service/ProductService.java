package com.meta.stock.product.service;

import com.meta.stock.config.PythonParser;
import com.meta.stock.lots.mapper.LotsMapper;
import com.meta.stock.materials.dto.MaterialDto;
import com.meta.stock.materials.dto.MaterialRequirementDto;
import com.meta.stock.materials.mapper.MaterialMapper;
import com.meta.stock.product.dto.*;
import com.meta.stock.product.entity.FixedProductEntity;
import com.meta.stock.product.entity.ProductEntity;
import com.meta.stock.product.mapper.ProductionRequestMapper;
import com.meta.stock.product.repository.FixedProductRepository;
import com.meta.stock.product.repository.ProductRepository;
import com.meta.stock.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private ProductionRequestMapper productionRequestMapper;
    @Autowired
    private LotsMapper lotsMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FixedProductRepository fixedProductRepository;
    @Autowired
    private PythonParser parser;

    public List<ProductStockDto> findTotalProductStock() {
        return productMapper.findTotalProductStock();
    }

    public List<FixedProductDto> getFixedProductWithStockQty() {
        return productMapper.getFixedProductWithStockQty();
    }

    public Map<String, String> getLossPrediction() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String todayYM = today.format(formatter);
        return parser.getLossContents(todayYM);
    }

    public Page<ProductStockDto> findProductStockWithPaging(String keyword, Pageable pageable) {
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

    //  주문 페이지용 제품 목록 조회
    @Transactional(readOnly = true)
    public List<ProductListDTO> getProductsForOrder() {
        List<FixedProductEntity> getProducts = fixedProductRepository.findAll();
        List<ProductListDTO> list = new ArrayList<>();
        for (FixedProductEntity product : getProducts) {
            list.add(new ProductListDTO(product.getSerialCode(), product.getName()));
        }
        return list;
    }

    public List<MaterialDto> getRequiredMaterials(Long fpId) {
        return materialMapper.getRequiredMaterials(fpId);
    }

    public int getProductionLoss(Long fpId) {
        return productMapper.getProductionLoss(fpId);
    }

    @Transactional
    public void produceProduct(Long fpId, Integer qty) {
        FixedProductDto fDto = productMapper.getNameAndLifeTime(fpId);

        int lossQty = parser.getLossPerProductAndYearMonth(fDto.getName(), qty);
        lotsMapper.storeProduct(qty - lossQty, fDto.getLifeTime());
        Long lotsId = lotsMapper.getLatestLot();
        Long prId = productionRequestMapper.getPrId(fpId);
        prId = (prId == null) ? 0 : prId;
        productMapper.produceProduct(fDto.getName(), lossQty, prId, lotsId);
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
