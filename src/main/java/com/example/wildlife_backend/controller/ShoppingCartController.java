package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartGetDto;
import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartItemCreateDto;
import com.example.wildlife_backend.service.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/shopping-cart")
@CrossOrigin
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    // Get shopping cart for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ShoppingCartGetDto> getShoppingCart(@PathVariable Long userId) {
        try {
            ShoppingCartGetDto cart = shoppingCartService.getShoppingCartByUserId(userId);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Add item to cart
    @PostMapping("/user/{userId}/items")
    public ResponseEntity<ShoppingCartGetDto> addItemToCart(
            @PathVariable Long userId,
            @Valid @RequestBody ShoppingCartItemCreateDto itemDto) {
        ShoppingCartGetDto updatedCart = shoppingCartService.addItemToCart(userId, itemDto);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    // Update item quantity in cart
    @PutMapping("/user/{userId}/items/{productItemId}")
    public ResponseEntity<ShoppingCartGetDto> updateCartItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long productItemId,
            @RequestParam Integer quantity) {
        try {
            ShoppingCartGetDto updatedCart = shoppingCartService.updateCartItemQuantity(userId, productItemId, quantity);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Remove item from cart
    @DeleteMapping("/user/{userId}/items/{productItemId}")
    public ResponseEntity<ShoppingCartGetDto> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable Long productItemId) {
        try {
            ShoppingCartGetDto updatedCart = shoppingCartService.removeItemFromCart(userId, productItemId);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Clear cart
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        try {
            shoppingCartService.clearCart(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get cart item count
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Integer> getCartItemCount(@PathVariable Long userId) {
        try {
            int count = shoppingCartService.getCartItemCount(userId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Validate cart item
    @PostMapping("/user/{userId}/validate-item")
    public ResponseEntity<String> validateCartItem(
            @PathVariable Long userId,
            @Valid @RequestBody ShoppingCartItemCreateDto itemDto) {
        boolean isValid = shoppingCartService.validateCartItem(userId, itemDto);
        if (isValid) {
            return new ResponseEntity<>("Cart item data is valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid cart item data", HttpStatus.BAD_REQUEST);
    }

    // Get carts with specific product item
    @GetMapping("/by-product/{productItemId}")
    public ResponseEntity<List<ShoppingCartGetDto>> getCartsByProductItemId(@PathVariable Long productItemId) {
        List<ShoppingCartGetDto> carts = shoppingCartService.getCartsByProductItemId(productItemId);
        if (carts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    // Calculate cart total
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<BigDecimal> getCartTotal(@PathVariable Long userId) {
        try {
            BigDecimal total = shoppingCartService.calculateCartTotal(userId);
            return new ResponseEntity<>(total, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Bulk add items to cart
    @PostMapping("/user/{userId}/bulk-items")
    public ResponseEntity<ShoppingCartGetDto> bulkAddItemsToCart(
            @PathVariable Long userId,
            @Valid @RequestBody List<ShoppingCartItemCreateDto> itemDtos) {
        ShoppingCartGetDto updatedCart = shoppingCartService.bulkAddItemsToCart(userId, itemDtos);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }
}
