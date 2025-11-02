package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.wishList.WishlistCreateDto;
import com.example.wildlife_backend.dto.wishList.WishlistResponseDto;
import com.example.wildlife_backend.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<WishlistResponseDto> addToWishlist(@RequestBody WishlistCreateDto wishlistDto) {
        WishlistResponseDto addedItem = wishlistService.addToWishlist(wishlistDto);
        return ResponseEntity.ok(addedItem);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishlistResponseDto>> getUserWishlist(@PathVariable Long userId) {
        List<WishlistResponseDto> wishlist = wishlistService.getUserWishlist(userId);
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping("/user/{userId}/photo/{photoId}/exists")
    public ResponseEntity<Boolean> checkIfPhotoInWishlist(@PathVariable Long userId, @PathVariable Long photoId) {
        boolean exists = wishlistService.isPhotoInUserWishlist(userId, photoId);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long wishlistId) {
        wishlistService.removeFromWishlist(wishlistId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/{userId}/photo/{photoId}")
    public ResponseEntity<Void> removePhotoFromWishlist(@PathVariable Long userId, @PathVariable Long photoId) {
        wishlistService.removePhotoFromWishlist(userId, photoId);
        return ResponseEntity.ok().build();
    }
}
