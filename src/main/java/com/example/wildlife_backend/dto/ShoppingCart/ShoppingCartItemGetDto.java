package com.example.wildlife_backend.dto.ShoppingCart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartItemGetDto {
    private Long id;
    private Long productItemId;
    private String productItemName;
    private String productItemSku;
    private String productItemImageUrl;
    private BigDecimal productItemPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
}
