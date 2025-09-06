package com.example.wildlife_backend.dto.ShoppingCart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartGetDto {
    private Long id;
    private Long userId;
    private List<ShoppingCartItemGetDto> items;
    private Integer totalItems;
    private Double totalPrice;
}
