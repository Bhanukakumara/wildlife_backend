package com.example.wildlife_backend.dto.UserPaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserPaymentMethodGetDto {
    private Long id;
    private Long userId;
    private Long paymentTypeId;
    private String provider;
    private int accountNumber;
    private LocalDateTime expiryDate;
    private boolean isDefault;
}