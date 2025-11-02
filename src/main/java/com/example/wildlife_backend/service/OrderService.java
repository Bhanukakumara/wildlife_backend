package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.order.OrderCreateDto;
import com.example.wildlife_backend.dto.order.OrderResponseDto;
import com.example.wildlife_backend.dto.order.OrderUpdateDto;
import com.example.wildlife_backend.util.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    // CRUD operations
    OrderResponseDto createOrder(OrderCreateDto orderDto);

    OrderResponseDto updateOrder(Long id, OrderUpdateDto orderDto);

    Optional<OrderResponseDto> getOrderById(Long id);

    Optional<OrderResponseDto> getOrderByOrderNumber(String orderNumber);

    List<OrderResponseDto> getAllOrders();

    void deleteOrder(Long id);

    // Business logic methods
    OrderResponseDto placeOrder(OrderCreateDto orderDto);

    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status);

    OrderResponseDto cancelOrder(Long orderId, String reason);

    OrderResponseDto shipOrder(Long orderId, String trackingNumber);

    OrderResponseDto deliverOrder(Long orderId);

    OrderResponseDto refundOrder(Long orderId, BigDecimal refundAmount);

    List<OrderResponseDto> getOrdersByUser(Long userId);

    List<OrderResponseDto> getOrdersByStatus(OrderStatus status);

    List<OrderResponseDto> getPendingOrders();

    List<OrderResponseDto> getProcessingOrders();

    OrderResponseDto addOrderItem(Long orderId, com.example.wildlife_backend.entity.OrderItem item);

    OrderResponseDto removeOrderItem(Long orderId, Long itemId);

    // Statistics methods
    BigDecimal getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);

    long getOrderCount(LocalDateTime startDate, LocalDateTime endDate);

    // Validation methods
    boolean isOrderNumberAvailable(String orderNumber);
}
