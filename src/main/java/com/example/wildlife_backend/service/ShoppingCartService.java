package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.ShoppingCartGetDto;
import com.example.wildlife_backend.dto.ShoppingCartItemCreateDto;

public interface ShoppingCartService {
    
    // Core shopping cart operations
    ShoppingCartGetDto getShoppingCartByUserId(Long userId);
    ShoppingCartGetDto addItemToCart(Long userId, ShoppingCartItemCreateDto itemDto);
    ShoppingCartGetDto updateCartItemQuantity(Long userId, Long productItemId, Integer quantity);
    ShoppingCartGetDto removeItemFromCart(Long userId, Long productItemId);
    void clearCart(Long userId);
    
    // Utility operations
    boolean existsByUserId(Long userId);
    int getCartItemCount(Long userId);
}
