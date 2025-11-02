package com.example.wildlife_backend.dto.payment;

import com.example.wildlife_backend.util.PaymentMethod;
import com.example.wildlife_backend.util.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {

    private Long id;
    private String paymentReference;
    private Long orderId;
    private String orderNumber;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String gatewayTransactionId;
    private String gatewayResponse;
    private String failureReason;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
