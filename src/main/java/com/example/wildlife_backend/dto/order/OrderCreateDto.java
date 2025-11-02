package com.example.wildlife_backend.dto.order;

import com.example.wildlife_backend.dto.orderItem.OrderItemCreateDto;
import com.example.wildlife_backend.util.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Order number is required")
    private String orderNumber;

    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemCreateDto> items;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Currency is required")
    private String currency;

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
}
