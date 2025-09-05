package com.example.wildlife_backend.service;

import com.example.wildlife_backend.entity.ShoppingCart;
import com.example.wildlife_backend.entity.ShoppingCartItem;
import java.util.Set;

public interface ShoppingCartService {
    ShoppingCart getCartByUserId(Long userId);

    ShoppingCart addItemToCart(Long userId, Long productId, int quantity);

    ShoppingCart removeItemFromCart(Long userId, Long itemId);

    Set<ShoppingCartItem> getItemsInCart(Long userId);

    void clearCart(Long userId);
}
