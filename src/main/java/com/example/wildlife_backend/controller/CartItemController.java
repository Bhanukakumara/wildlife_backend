package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.cartItem.CartItemDto;
import com.example.wildlife_backend.dto.cartItem.CartItemUpdateDto;
import com.example.wildlife_backend.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemDto>> getCartItemsByCartId(@PathVariable Long cartId) {
        List<CartItemDto> items = cartItemService.getCartItemsByCartId(cartId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItemDto> getCartItemById(@PathVariable Long id) {
        Optional<CartItemDto> item = cartItemService.getCartItemById(id);
        return item.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemDto> updateCartItem(@PathVariable Long id, @RequestBody CartItemUpdateDto cartItemDto) {
        CartItemDto updatedItem = cartItemService.updateCartItem(id, cartItemDto);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.ok().build();
    }
}
