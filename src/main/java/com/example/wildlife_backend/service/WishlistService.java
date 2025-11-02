package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.wishList.WishlistCreateDto;
import com.example.wildlife_backend.dto.wishList.WishlistResponseDto;

import java.util.List;

public interface WishlistService {
    WishlistResponseDto addToWishlist(WishlistCreateDto wishlistDto);
    void removeFromWishlist(Long wishlistId);
    List<WishlistResponseDto> getUserWishlist(Long userId);
    boolean isPhotoInUserWishlist(Long userId, Long photoId);
    void removePhotoFromWishlist(Long userId, Long photoId);
}
