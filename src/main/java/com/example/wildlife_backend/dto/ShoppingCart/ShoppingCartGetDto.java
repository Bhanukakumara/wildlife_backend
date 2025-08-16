package com.example.wildlife_backend.dto.ShoppingCart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartGetDto {
    private Long id;
    private Long userId;
    private List<ShoppingCartItemGetDto> items;
    private Integer totalItems;
    private BigDecimal totalPrice;
}
