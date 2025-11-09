package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.cart.CartDto;
import com.example.wildlife_backend.dto.cartItem.CartItemCreateDto;
import com.example.wildlife_backend.dto.cartItem.CartItemDto;
import com.example.wildlife_backend.entity.Cart;
import com.example.wildlife_backend.entity.CartItem;
import com.example.wildlife_backend.entity.Photo;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.repository.CartRepository;
import com.example.wildlife_backend.repository.PhotoRepository;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    @Override
    public CartDto getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
            .orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setUser(user);
                return cartRepository.save(newCart);
            });
        return convertToCartDto(cart);
    }

    @Override
    public CartDto addItemToCart(Long userId, CartItemCreateDto itemDto) {
        Cart cart = getOrCreateCartEntity(userId);

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getPhoto().getId().equals(itemDto.getPhotoId()) &&
                           item.getLicenseType().equals(itemDto.getLicenseType()))
            .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity of existing item
            existingItem.get().setQuantity(existingItem.get().getQuantity() + itemDto.getQuantity());
        } else {
            // Create new cart item
            Photo photo = photoRepository.findById(itemDto.getPhotoId())
                .orElseThrow(() -> new RuntimeException("Photo not found"));

            CartItem newItem = new CartItem();
            newItem.setPhoto(photo);
            newItem.setLicenseType(itemDto.getLicenseType());
            newItem.setQuantity(itemDto.getQuantity());
            newItem.setUnitPrice(photo.getBasePrice()); // You might want to adjust based on license type

            cart.addCartItem(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return convertToCartDto(savedCart);
    }

    @Override
    public CartDto updateCartItemQuantity(Long userId, Long itemId, Integer quantity) {
        Cart cart = getOrCreateCartEntity(userId);
        CartItem item = cart.getItems().stream()
            .filter(cartItem -> cartItem.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Cart item not found"));

        item.setQuantity(quantity);
        Cart savedCart = cartRepository.save(cart);
        return convertToCartDto(savedCart);
    }

    @Override
    public CartDto removeItemFromCart(Long userId, Long itemId) {
        Cart cart = getOrCreateCartEntity(userId);
        CartItem item = cart.getItems().stream()
            .filter(cartItem -> cartItem.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.removeCartItem(item);
        Cart savedCart = cartRepository.save(cart);
        return convertToCartDto(savedCart);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCartEntity(userId);
        cart.getItems().clear();
        cart.setTotalItems(0);
        cart.setTotalAmount(java.math.BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    @Override
    public Optional<CartDto> getCartByUserId(Long userId) {
        return userRepository.findById(userId)
            .flatMap(cartRepository::findByUser)
            .map(this::convertToCartDto);
    }

    private Cart getOrCreateCartEntity(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
            .orElseGet(() -> {
                Cart cart = new Cart();
                cart.setUser(user);
                return cartRepository.save(cart);
            });
    }

    private CartDto convertToCartDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setTotalItems(cart.getTotalItems());
        dto.setTotalAmount(cart.getTotalAmount());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        dto.setItems(cart.getItems().stream()
            .map(this::convertToCartItemDto)
            .collect(Collectors.toList()));
        return dto;
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
