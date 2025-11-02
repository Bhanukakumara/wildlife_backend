package com.example.wildlife_backend.dto.cart;

import com.example.wildlife_backend.dto.cartItem.CartItemDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartDto {

    private Long id;
    private Long userId;
    private Integer totalItems;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemDto> items;
}
