package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.entity.ShoppingCart;
import com.example.wildlife_backend.entity.ShoppingCartItem;
import com.example.wildlife_backend.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/shopping-cart")
@CrossOrigin
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    // Get cart by user id
    @GetMapping("/get-by-user-id/{userId}")
    public ResponseEntity<ShoppingCart> getCartByUserId(@PathVariable Long userId) {
        ShoppingCart cart = shoppingCartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    // Add item to cart
    @PostMapping("/{userId}/items")
    public ResponseEntity<ShoppingCart> addItemToCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        ShoppingCart updatedCart = shoppingCartService.addItemToCart(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // Remove item from cart
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ShoppingCart> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable Long itemId
    ) {
        ShoppingCart updatedCart = shoppingCartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    // Get all items in cart
    @GetMapping("/{userId}/items")
    public ResponseEntity<Set<ShoppingCartItem>> getItemsInCart(@PathVariable Long userId) {
        Set<ShoppingCartItem> items = shoppingCartService.getItemsInCart(userId);
        return ResponseEntity.ok(items);
    }

    // Clear cart
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        shoppingCartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
