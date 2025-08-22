package com.example.wildlife_backend.dto.ShoppingCart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ShoppingCartItemCreateDto {
    private Long shoppingCartId;
    private Long productItemId;
    private int quantity;
}
