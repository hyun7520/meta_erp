package com.meta.stock.product;

import com.meta.stock.product.DTO.OrderCreateDTO;
import com.meta.stock.product.DTO.OrderDTO;
import com.meta.stock.product.DTO.TopProductDTO;
import com.meta.stock.product.Entity.OrderEntity;
import com.meta.stock.product.Entity.ProductsEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private static final int MAX_RECORDS = 30;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // 전체 주문 조회
    public List<OrderDTO> getAllOrders() {
        deleteOldRecordsIfNeeded();
        return orderRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 미완료 주문 조회
    public List<OrderDTO> getIncompleteOrders() {
        deleteOldRecordsIfNeeded();
        return orderRepository.findByComplete(0)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 주문 생성
    @Transactional
    public OrderDTO createOrder(OrderCreateDTO dto) {
        ProductsEntity product = productRepository.findById(Long.valueOf(dto.getProductId()))
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다."));

        OrderEntity order = new OrderEntity();
        order.setProductId(dto.getProductId());
        order.setRequestBy(dto.getRequestBy());
        order.setQty(dto.getQty());
        order.setUnit(dto.getUnit());
        order.setRequestDate(dto.getRequestDate());
        order.setDeadline(dto.getDeadline());
        order.setComplete(0);

        OrderEntity saved = orderRepository.save(order);
        deleteOldRecordsIfNeeded();
        return convertToDTO(saved);
    }

    @Transactional
    protected void deleteOldRecordsIfNeeded() {
        long count = orderRepository.count();

        if (count > MAX_RECORDS) {
            int deleteCount = (int)(count - MAX_RECORDS);

            // 가장 오래된 레코드 조회 (요청일 기준)
            List<OrderEntity> oldestRecords = orderRepository
                    .findAll()
                    .stream()
                    .sorted((a, b) -> a.getRequestDate().compareTo(b.getRequestDate()))
                    .limit(deleteCount)
                    .collect(Collectors.toList());

            orderRepository.deleteAll(oldestRecords);

            System.out.println("오래된 완제품 주문 " + deleteCount + "건 삭제됨");
        }
    }


    // 최근 한 달간 가장 많이 주문된 상품 조회
    public TopProductDTO getTopProductLastMonth() {
        // 한 달 전 날짜 계산
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        String dateStr = oneMonthAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 최근 한 달간의 모든 주문 가져오기
        List<OrderEntity> recentOrders = orderRepository.findAll()
                .stream()
                .filter(order -> order.getRequestDate() != null &&
                        order.getRequestDate().compareTo(dateStr) >= 0)
                .collect(Collectors.toList());

        if (recentOrders.isEmpty()) {
            return null;
        }

        // 상품별로 수량 합계 계산
        var productQtyMap = recentOrders.stream()
                .collect(Collectors.groupingBy(
                        OrderEntity::getProductId,
                        Collectors.summingInt(OrderEntity::getQty)
                ));

        // 가장 많이 주문된 상품 찾기
        Long topProductId = productQtyMap.entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .map(e -> e.getKey())
                .orElse(null);

        if (topProductId == null) {
            return null;
        }

        // 상품 정보 조회
        ProductsEntity product = productRepository.findById(topProductId).orElse(null);
        if (product == null) {
            return null;
        }

        // 해당 상품의 총 수량과 주문 건수 계산
        int totalQty = productQtyMap.get(topProductId);
        long orderCount = recentOrders.stream()
                .filter(order -> order.getProductId() == topProductId)
                .count();

        // 단위 가져오기 (첫 번째 주문의 단위 사용)
        String unit = recentOrders.stream()
                .filter(order -> order.getProductId() == topProductId)
                .findFirst()
                .map(OrderEntity::getUnit)
                .orElse("개");

        return new TopProductDTO(
                product.getProductName(),
                totalQty,
                (int) orderCount,
                unit
        );
    }

    // Entity -> DTO 변환
    private OrderDTO convertToDTO(OrderEntity entity) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(entity.getOrderId());
        dto.setRequestBy(entity.getRequestBy());
        dto.setQty(entity.getQty());
        dto.setUnit(entity.getUnit());
        dto.setRequestDate(entity.getRequestDate());
        dto.setDeadline(entity.getDeadline());
        dto.setComplete(entity.getComplete());

        // 제품 이름 조회
        if (entity.getProductId() != 0) {
            productRepository.findById(Long.valueOf(entity.getProductId()))
                    .ifPresent(product -> dto.setProductName(product.getProductName()));
        }

        // 상태 계산
        if (entity.getComplete() == 1) {
            dto.setStatus("완료");
        } else {
            dto.setStatus("진행중");
        }

        return dto;
    }
}