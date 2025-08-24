package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartGetDto;
import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartItemCreateDto;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

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

    boolean validateCartItem(Long userId, @Valid ShoppingCartItemCreateDto itemDto);

    List<ShoppingCartGetDto> getCartsByProductItemId(Long productItemId);

    BigDecimal calculateCartTotal(Long userId);

    ShoppingCartGetDto bulkAddItemsToCart(Long userId, @Valid List<ShoppingCartItemCreateDto> itemDtos);
}
