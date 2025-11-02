package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.cart.CartDto;
import com.example.wildlife_backend.dto.cartItem.CartItemCreateDto;

import java.util.Optional;

public interface CartService {
    CartDto getOrCreateCart(Long userId);
    CartDto addItemToCart(Long userId, CartItemCreateDto itemDto);
    CartDto updateCartItemQuantity(Long userId, Long itemId, Integer quantity);
    CartDto removeItemFromCart(Long userId, Long itemId);
    void clearCart(Long userId);
    Optional<CartDto> getCartByUserId(Long userId);
}
