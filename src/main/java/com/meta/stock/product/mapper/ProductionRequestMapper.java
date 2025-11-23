package com.meta.stock.product.mapper;

import com.meta.stock.product.dto.ProductRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ProductionRequestMapper {

    List<ProductRequestDto> findAllRequestsWithPaging(String keyword, String sortBy, String sortDir, int offset, int limit);

    long countAllRequests(String keyword);

    List<ProductRequestDto> findOngoingRequestsWithPaging(String keyword, String sortBy, String sortDir, int offset, int limit);

    long countOngoingRequests(String keyword);

    ProductRequestDto findProductRequestById(long orderId);


    void updateEndDate(Long prId);

    void updateProductionStartDate(@Param("employeeId") Long employeeId, @Param("prId") Long prId);

    int getOngoingRequestsCount(String keyword);

    int countCompleted();

    int countOverdue();

    int countOngoing();

    int countPending();
}
