package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.wishList.WishlistCreateDto;
import com.example.wildlife_backend.dto.wishList.WishlistResponseDto;
import com.example.wildlife_backend.entity.Wishlist;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.entity.Photo;
import com.example.wildlife_backend.repository.WishlistRepository;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.repository.PhotoRepository;
import com.example.wildlife_backend.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    @Override
    public WishlistResponseDto addToWishlist(WishlistCreateDto wishlistDto) {
        User user = userRepository.findById(wishlistDto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Photo photo = photoRepository.findById(wishlistDto.getPhotoId())
            .orElseThrow(() -> new RuntimeException("Photo not found"));

        if (wishlistRepository.existsByUserAndPhoto(user, photo)) {
            throw new RuntimeException("Photo already in wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setPhoto(photo);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return convertToWishlistResponseDto(savedWishlist);
    }

    @Override
    public void removeFromWishlist(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WishlistResponseDto> getUserWishlist(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return wishlistRepository.findByUser(user).stream()
            .map(this::convertToWishlistResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPhotoInUserWishlist(Long userId, Long photoId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Photo photo = photoRepository.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Photo not found"));
        return wishlistRepository.existsByUserAndPhoto(user, photo);
    }

    @Override
    public void removePhotoFromWishlist(Long userId, Long photoId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Photo photo = photoRepository.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Photo not found"));

        Optional<Wishlist> wishlist = wishlistRepository.findByUserAndPhoto(user, photo);
        wishlist.ifPresent(wishlistRepository::delete);
    }

    private WishlistResponseDto convertToWishlistResponseDto(Wishlist wishlist) {
        WishlistResponseDto dto = new WishlistResponseDto();
        dto.setId(wishlist.getId());
        dto.setUserId(wishlist.getUser().getId());
        dto.setUserUsername(wishlist.getUser().getUsername());
        dto.setUserFullName(wishlist.getUser().getFullName());
        dto.setPhotoId(wishlist.getPhoto().getId());
        dto.setPhotoTitle(wishlist.getPhoto().getTitle());
        dto.setPhotoImageUrl(wishlist.getPhoto().getImageUrl());
        dto.setPhotoBasePrice(wishlist.getPhoto().getBasePrice());
        dto.setAddedAt(wishlist.getAddedAt());
        return dto;
    }
}
