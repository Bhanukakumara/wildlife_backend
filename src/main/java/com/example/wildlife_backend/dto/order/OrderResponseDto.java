package com.example.wildlife_backend.dto.order;

import com.example.wildlife_backend.dto.orderItem.OrderItemDto;
import com.example.wildlife_backend.util.OrderStatus;
import com.example.wildlife_backend.util.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {

    private Long id;
    private String orderNumber;
    private Long userId;
    private String userUsername;
    private String userEmail;
    private BigDecimal totalAmount;
    private BigDecimal subtotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingAmount;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private String currency;
    private String paymentStatus;
    private String paymentId;
    private String transactionId;
    private String billingName;
    private String billingEmail;
    private String billingPhone;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingZip;
    private String billingCountry;
    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String shippingCountry;
    private String notes;
    private String trackingNumber;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private LocalDateTime refundedAt;
    private BigDecimal refundAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemDto> items;
}
