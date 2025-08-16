package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartGetDto;
import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartItemCreateDto;
import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartItemGetDto;
import com.example.wildlife_backend.entity.ProductItem;
import com.example.wildlife_backend.entity.ShoppingCart;
import com.example.wildlife_backend.entity.ShoppingCartItem;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.ProductItemRepository;
import com.example.wildlife_backend.repository.ShoppingCartItemRepository;
import com.example.wildlife_backend.repository.ShoppingCartRepository;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final UserRepository userRepository;
    private final ProductItemRepository productItemRepository;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartGetDto getShoppingCartByUserId(Long userId) {
        ShoppingCart shoppingCart = getOrCreateShoppingCart(userId);
        List<ShoppingCartItemGetDto> items = getCartItems(shoppingCart.getId());
        
        int totalItems = items.stream().mapToInt(ShoppingCartItemGetDto::getQuantity).sum();
        BigDecimal totalPrice = items.stream()
                .map(ShoppingCartItemGetDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new ShoppingCartGetDto(
                shoppingCart.getId(),
                userId,
                items,
                totalItems,
                totalPrice
        );
    }

    @Override
    @Transactional
    public ShoppingCartGetDto addItemToCart(Long userId, ShoppingCartItemCreateDto itemDto) {
        ShoppingCart shoppingCart = getOrCreateShoppingCart(userId);
        
        // Check if product item exists and has sufficient stock
        ProductItem productItem = productItemRepository.findById(itemDto.getProductItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Product item not found with id: " + itemDto.getProductItemId()));
        
        if (productItem.getQtyInStock() < itemDto.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for product item: " + productItem.getName());
        }
        
        // Check if item already exists in cart
        ShoppingCartItem existingItem = shoppingCartItemRepository
                .findByShoppingCartIdAndProductItemId(shoppingCart.getId(), itemDto.getProductItemId())
                .orElse(null);
        
        if (existingItem != null) {
            // Update quantity
            existingItem.setQuantity(existingItem.getQuantity() + itemDto.getQuantity());
            shoppingCartItemRepository.save(existingItem);
        } else {
            // Create new cart item
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setShoppingCart(shoppingCart);
            newItem.setProductItem(productItem);
            newItem.setQuantity(itemDto.getQuantity());
            shoppingCartItemRepository.save(newItem);
        }
        
        log.info("Added item to cart for user {}: product item {} with quantity {}", 
                userId, itemDto.getProductItemId(), itemDto.getQuantity());
        
        return getShoppingCartByUserId(userId);
    }

    @Override
    @Transactional
    public ShoppingCartGetDto updateCartItemQuantity(Long userId, Long productItemId, Integer quantity) {
        ShoppingCart shoppingCart = getOrCreateShoppingCart(userId);
        
        ShoppingCartItem cartItem = shoppingCartItemRepository
                .findByShoppingCartIdAndProductItemId(shoppingCart.getId(), productItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        
        if (quantity <= 0) {
            shoppingCartItemRepository.delete(cartItem);
        } else {
            // Check stock availability
            ProductItem productItem = cartItem.getProductItem();
            if (productItem.getQtyInStock() < quantity) {
                throw new IllegalArgumentException("Insufficient stock for product item: " + productItem.getName());
            }
            
            cartItem.setQuantity(quantity);
            shoppingCartItemRepository.save(cartItem);
        }
        
        log.info("Updated cart item quantity for user {}: product item {} with quantity {}", 
                userId, productItemId, quantity);
        
        return getShoppingCartByUserId(userId);
    }

    @Override
    @Transactional
    public ShoppingCartGetDto removeItemFromCart(Long userId, Long productItemId) {
        ShoppingCart shoppingCart = getOrCreateShoppingCart(userId);
        
        ShoppingCartItem cartItem = shoppingCartItemRepository
                .findByShoppingCartIdAndProductItemId(shoppingCart.getId(), productItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        
        shoppingCartItemRepository.delete(cartItem);
        
        log.info("Removed item from cart for user {}: product item {}", userId, productItemId);
        
        return getShoppingCartByUserId(userId);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        ShoppingCart shoppingCart = getOrCreateShoppingCart(userId);
        shoppingCartItemRepository.deleteByShoppingCartId(shoppingCart.getId());
        
        log.info("Cleared cart for user {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserId(Long userId) {
        return shoppingCartRepository.existsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCartItemCount(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElse(null);
        if (shoppingCart == null) {
            return 0;
        }
        return shoppingCartItemRepository.countByShoppingCartId(shoppingCart.getId());
    }

    private ShoppingCart getOrCreateShoppingCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                    
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(user);
                    return shoppingCartRepository.save(newCart);
                });
    }

    private List<ShoppingCartItemGetDto> getCartItems(Long cartId) {
        List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByShoppingCartId(cartId);
        
        return cartItems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ShoppingCartItemGetDto convertToDto(ShoppingCartItem cartItem) {
        ProductItem productItem = cartItem.getProductItem();
        BigDecimal totalPrice = productItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        
        return new ShoppingCartItemGetDto(
                cartItem.getId(),
                productItem.getId(),
                productItem.getName(),
                productItem.getSku(),
                productItem.getImageUrl(),
                productItem.getPrice(),
                cartItem.getQuantity(),
                totalPrice
        );
    }
}
