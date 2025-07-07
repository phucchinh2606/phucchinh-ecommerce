package com.phucchinh.ecommerce.serivce.impl;

import com.phucchinh.ecommerce.dto.OrderItemDto;
import com.phucchinh.ecommerce.dto.OrderRequest;
import com.phucchinh.ecommerce.dto.Response;
import com.phucchinh.ecommerce.ecxeption.NotFoundException;
import com.phucchinh.ecommerce.entity.Order;
import com.phucchinh.ecommerce.entity.OrderItem;
import com.phucchinh.ecommerce.entity.Product;
import com.phucchinh.ecommerce.entity.User;
import com.phucchinh.ecommerce.enums.OrderStatus;
import com.phucchinh.ecommerce.mapper.EntityDtoMapper;
import com.phucchinh.ecommerce.repository.OrderItemRepo;
import com.phucchinh.ecommerce.repository.OrderRepo;
import com.phucchinh.ecommerce.repository.ProductRepo;
import com.phucchinh.ecommerce.serivce.interf.OrderItemService;
import com.phucchinh.ecommerce.serivce.interf.UserService;
import com.phucchinh.ecommerce.specification.OrderItemSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response placeOrder(OrderRequest orderRequest) {

        User user = userService.getLoginUser();
        //map orderRequest to order entities
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(
                orderItemRequest -> {
                    Product product = productRepo.findById(orderItemRequest.getProductId())
                            .orElseThrow(()-> new NotFoundException("Product not found"));
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setQuantity(orderItemRequest.getQuantity());
                    orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity())));
                    orderItem.setStatus(OrderStatus.PENDING);
                    orderItem.setUser(user);
                    return orderItem;
                }).toList();

        // Tính tổng giá
        BigDecimal totalPrice = orderRequest.getTotalPrice() != null && orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO)>0
                ? orderRequest.getTotalPrice()
                : orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //create order entity
        Order order = new Order();
        order.setOrderItemList(orderItems);
        order.setTotalPrice(totalPrice);

        //set the order reference in each orderitem
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        orderRepo.save(order);
        return Response.builder()
                .status(200)
                .message("Order was placed successfully")
                .build();
    }

    @Override
    public Response updateOrderItemStatus(Long orderItemId, String status) {
        OrderItem orderItem = orderItemRepo.findById(orderItemId).orElseThrow(
                ()->new NotFoundException("Order item not found")
        );
        orderItem.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderItemRepo.save(orderItem);
        return Response.builder()
                .status(200)
                .message("Order status updated successfully")
                .build();
    }

    @Override
    public Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable) {
        Specification<OrderItem> spec = Specification.where(OrderItemSpecification.hasStatus(status))
                .and(OrderItemSpecification.createdBetween(startDate,endDate))
                .and(OrderItemSpecification.hasItemId(itemId));

        Page<OrderItem> orderItemPage = orderItemRepo.findAll(spec,pageable);
        if(orderItemPage.isEmpty()){
            throw new NotFoundException("No order found");
        }
        List<OrderItemDto> orderItemDtos = orderItemPage.getContent().stream()
                .map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser).toList();
        return Response.builder()
                .status(200)
                .orderItemList(orderItemDtos)
                .totalPage(orderItemPage.getTotalPages())
                .totalElement(orderItemPage.getTotalElements())
                .build();
    }
}
