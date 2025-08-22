package com.example.wildlife_backend.dto.ShippingMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ShippingMethodGetDto {
    private Long id;
    private String name;
    private BigDecimal price;
}