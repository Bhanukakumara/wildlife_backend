package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.cartItem.CartItemDto;
import com.example.wildlife_backend.dto.cartItem.CartItemUpdateDto;
import com.example.wildlife_backend.entity.CartItem;
import com.example.wildlife_backend.repository.CartItemRepository;
import com.example.wildlife_backend.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CartItemDto> getCartItemsByCartId(Long cartId) {
        // This would need Cart repository - for now returning all
        return cartItemRepository.findAll().stream()
            .map(this::convertToCartItemDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CartItemDto> getCartItemById(Long id) {
        return cartItemRepository.findById(id)
            .map(this::convertToCartItemDto);
    }

    @Override
    public CartItemDto updateCartItem(Long id, CartItemUpdateDto cartItemDto) {
        CartItem existingItem = cartItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));

        existingItem.setQuantity(cartItemDto.getQuantity());
        CartItem savedItem = cartItemRepository.save(existingItem);
        return convertToCartItemDto(savedItem);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    private CartItemDto convertToCartItemDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setId(item.getId());
        dto.setPhotoId(item.getPhoto().getId());
        dto.setPhotoTitle(item.getPhoto().getTitle());
        dto.setPhotoImageUrl(item.getPhoto().getImageUrl());
        dto.setPhotoBasePrice(item.getPhoto().getBasePrice());
        dto.setLicenseType(item.getLicenseType());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setTotalPrice(item.getTotalPrice());
        dto.setAddedAt(item.getAddedAt());
        return dto;
    }
}
