package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.cart.CartDto;
import com.example.wildlife_backend.dto.cartItem.CartItemCreateDto;
import com.example.wildlife_backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Long userId) {
        Optional<CartDto> cart = cartService.getCartByUserId(userId);
        return cart.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/user/{userId}/items")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable Long userId, @RequestBody CartItemCreateDto itemDto) {
        CartDto updatedCart = cartService.addItemToCart(userId, itemDto);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/user/{userId}/items/{itemId}")
    public ResponseEntity<CartDto> updateItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        CartDto updatedCart = cartService.updateCartItemQuantity(userId, itemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/user/{userId}/items/{itemId}")
    public ResponseEntity<CartDto> removeItemFromCart(@PathVariable Long userId, @PathVariable Long itemId) {
        CartDto updatedCart = cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
}
