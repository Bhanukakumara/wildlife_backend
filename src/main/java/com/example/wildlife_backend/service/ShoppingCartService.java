package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartGetDto;
import com.example.wildlife_backend.entity.ShoppingCart;
import com.example.wildlife_backend.entity.ShoppingCartItem;

import java.util.Set;

public interface ShoppingCartService {

    ShoppingCartGetDto getCartByUserId(Long userId);

    ShoppingCart addItemToCart(Long userId, Long productItemId, int quantity);

    ShoppingCart removeItemFromCart(Long userId, Long itemId);

    Set<ShoppingCartItem> getItemsInCart(Long userId);

    void clearCart(Long userId);

    Integer getItemCountByUserId(Long userId);
}