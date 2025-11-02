package com.example.wildlife_backend.dto.payment;

import com.example.wildlife_backend.util.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCreateDto {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Size(max = 3, message = "Currency must be 3 characters")
    private String currency = "USD";
}
