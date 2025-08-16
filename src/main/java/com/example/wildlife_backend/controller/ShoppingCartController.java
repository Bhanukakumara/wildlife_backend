package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartGetDto;
import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartItemCreateDto;
import com.example.wildlife_backend.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shopping-cart")
@CrossOrigin
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    // Get shopping cart for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ShoppingCartGetDto> getShoppingCart(@PathVariable Long userId) {
        ShoppingCartGetDto cart = shoppingCartService.getShoppingCartByUserId(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    // Add item to cart
    @PostMapping("/user/{userId}/items")
    public ResponseEntity<ShoppingCartGetDto> addItemToCart(
            @PathVariable Long userId,
            @RequestBody ShoppingCartItemCreateDto itemDto) {
        ShoppingCartGetDto updatedCart = shoppingCartService.addItemToCart(userId, itemDto);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    // Update item quantity in cart
    @PutMapping("/user/{userId}/items/{productItemId}")
    public ResponseEntity<ShoppingCartGetDto> updateCartItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long productItemId,
            @RequestParam Integer quantity) {
        ShoppingCartGetDto updatedCart = shoppingCartService.updateCartItemQuantity(userId, productItemId, quantity);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    // Remove item from cart
    @DeleteMapping("/user/{userId}/items/{productItemId}")
    public ResponseEntity<ShoppingCartGetDto> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable Long productItemId) {
        ShoppingCartGetDto updatedCart = shoppingCartService.removeItemFromCart(userId, productItemId);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    // Clear cart
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        shoppingCartService.clearCart(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get cart item count
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Integer> getCartItemCount(@PathVariable Long userId) {
        int count = shoppingCartService.getCartItemCount(userId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
