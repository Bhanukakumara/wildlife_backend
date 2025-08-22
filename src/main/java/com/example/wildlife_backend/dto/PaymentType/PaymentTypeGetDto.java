package com.example.wildlife_backend.dto.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentTypeGetDto {
    private Long id;
    private String value;
}