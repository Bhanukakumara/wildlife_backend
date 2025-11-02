package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.cartItem.CartItemDto;
import com.example.wildlife_backend.dto.cartItem.CartItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface CartItemService {
    List<CartItemDto> getCartItemsByCartId(Long cartId);
    Optional<CartItemDto> getCartItemById(Long id);
    CartItemDto updateCartItem(Long id, CartItemUpdateDto cartItemDto);
    void deleteCartItem(Long id);
}
