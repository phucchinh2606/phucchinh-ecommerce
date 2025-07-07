package com.phucchinh.ecommerce.serivce.interf;

import com.phucchinh.ecommerce.dto.OrderRequest;
import com.phucchinh.ecommerce.dto.Response;
import com.phucchinh.ecommerce.enums.OrderStatus;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface OrderItemService {

    Response placeOrder(OrderRequest orderRequest);
    Response updateOrderItemStatus(Long orderItemId,String status);
    Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable);
}
