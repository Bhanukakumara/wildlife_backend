package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartGetDto;
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
    public ResponseEntity<ShoppingCartGetDto> getCartByUserId(@PathVariable Long userId) {
        ShoppingCartGetDto cartByUserId = shoppingCartService.getCartByUserId(userId);
        if (cartByUserId == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(cartByUserId);
        }
    }

    //get items count by user id
    @GetMapping("/get-item-count/{userId}")
    public ResponseEntity<Integer> getItemCountByUserId(@PathVariable Long userId) {
        Integer itemCountByUserId = shoppingCartService.getItemCountByUserId(userId);
        if (itemCountByUserId == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(itemCountByUserId);
        }
    }

    // Add item to cart
    @PostMapping("/add-item/{userId}/items")
    public ResponseEntity<ShoppingCart> addItemToCart(
            @PathVariable Long userId,
            @RequestParam Long productItemId,
            @RequestParam int quantity
    ) {
        ShoppingCart updatedCart = shoppingCartService.addItemToCart(userId, productItemId, quantity);
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
