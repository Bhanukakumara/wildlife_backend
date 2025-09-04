package com.example.wildlife_backend.dto.OrderLine;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderLineCreateDto {
    @NotNull(message = "Item is required")
    private Long productItemId;

    @NotNull(message = "Order is required")
    private Long orderId;

    @NotNull(message = "Quantity is required")
    private Long quantity;

    @NotNull(message = "Price is required")
    private BigDecimal price;
}
