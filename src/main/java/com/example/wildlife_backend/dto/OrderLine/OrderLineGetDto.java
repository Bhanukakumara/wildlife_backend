package com.example.wildlife_backend.dto.OrderLine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderLineGetDto {
    private Long id;
    private Long productItemId;
    private Long orderId;
    private Long quantity;
    private BigDecimal price;
}