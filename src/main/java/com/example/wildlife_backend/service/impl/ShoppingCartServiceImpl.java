package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartGetDto;
import com.example.wildlife_backend.dto.ShoppingCart.ShoppingCartItemGetDto;
import com.example.wildlife_backend.entity.ProductItem;
import com.example.wildlife_backend.entity.ShoppingCart;
import com.example.wildlife_backend.entity.ShoppingCartItem;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.repository.ProductItemRepository;
import com.example.wildlife_backend.repository.ShoppingCartItemRepository;
import com.example.wildlife_backend.repository.ShoppingCartRepository;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final UserRepository userRepository;
    private final ProductItemRepository productItemRepository;

    @Override
    public ShoppingCartGetDto getCartByUserId(Long userId) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(userId);
        if (shoppingCart.isPresent()) {
            // Fixed: Changed from findByCartId to findByShoppingCartId
            List<ShoppingCartItem> shoppingCartItems = shoppingCartItemRepository.findByShoppingCartId(shoppingCart.get().getId());
            List<ShoppingCartItemGetDto> shoppingCartItemGetDtos = new ArrayList<>();
            BigDecimal totalPrice = BigDecimal.ZERO; // Simplified - no need for array

            for (ShoppingCartItem shoppingCartItem : shoppingCartItems) {
                BigDecimal price = shoppingCartItem.getProductItem().getPrice();
                int quantity = shoppingCartItem.getQuantity();
                BigDecimal itemTotalPrice = price.multiply(BigDecimal.valueOf(quantity));

                shoppingCartItemGetDtos.add(ShoppingCartItemGetDto.builder()
                        .id(shoppingCartItem.getId())
                        .productItemId(shoppingCartItem.getProductItem().getId())
                        .productItemName(shoppingCartItem.getProductItem().getName())
                        .productItemSku(shoppingCartItem.getProductItem().getSku())
                        .productItemImageUrl(shoppingCartItem.getProductItem().getImageUrl())
                        .productItemPrice(price)
                        .quantity(quantity)
                        .totalPrice(itemTotalPrice)
                        .build());

                totalPrice = totalPrice.add(itemTotalPrice);
            }

            return ShoppingCartGetDto.builder()
                    .id(shoppingCart.get().getId())
                    .userId(shoppingCart.get().getUser().getId())
                    .items(shoppingCartItemGetDtos)
                    .totalItems(shoppingCartItems.size())
                    .totalPrice(totalPrice.doubleValue())
                    .build();
        }

        // Return empty cart when no cart exists
        return ShoppingCartGetDto.builder()
                .id(null)
                .userId(userId)
                .items(new ArrayList<>())
                .totalItems(0)
                .totalPrice(0.0)
                .build();
    }

    @Override
    public ShoppingCart addItemToCart(Long userId, Long productItemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        // Find or create a shopping cart for the user
        Optional<ShoppingCart> shoppingCartOpt = shoppingCartRepository.findByUserId(userId);
        ShoppingCart cart = shoppingCartOpt.orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUser(user);
            return shoppingCartRepository.save(newCart);
        });

        // Find the product item
        ProductItem productItem = productItemRepository.findById(productItemId)
                .orElseThrow(() -> new EntityNotFoundException("ProductItem not found with ID: " + productItemId));

        // Check if the item already exists in the cart
        Optional<ShoppingCartItem> existingItemOpt = cart.getShoppingCartItems().stream()
                .filter(item -> item.getProductItem().getId().equals(productItemId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            // Update existing item's quantity
            ShoppingCartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            shoppingCartItemRepository.save(existingItem);
        } else {
            // Create and add new item to the cart
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setProductItem(productItem);
            newItem.setQuantity(quantity);
            newItem.setShoppingCart(cart);
            cart.getShoppingCartItems().add(newItem);
            shoppingCartItemRepository.save(newItem);
        }

        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart removeItemFromCart(Long userId, Long itemId) {
        // Find the user's cart
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user ID: " + userId));

        // Find the item
        ShoppingCartItem item = shoppingCartItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + itemId));

        // Verify the item belongs to the user's cart
        if (!item.getShoppingCart().getId().equals(cart.getId())) {
            throw new IllegalStateException("Item does not belong to user's cart");
        }

        // Remove the item from the cart and delete it
        cart.getShoppingCartItems().remove(item);
        shoppingCartItemRepository.delete(item);

        return shoppingCartRepository.save(cart);
    }

    @Override
    public Set<ShoppingCartItem> getItemsInCart(Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user ID: " + userId));
        return cart.getShoppingCartItems() != null ? cart.getShoppingCartItems() : new HashSet<>();
    }

    @Override
    public void clearCart(Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user ID: " + userId));

        if (cart.getShoppingCartItems() != null && !cart.getShoppingCartItems().isEmpty()) {
            shoppingCartItemRepository.deleteAll(cart.getShoppingCartItems());
            cart.getShoppingCartItems().clear();
            shoppingCartRepository.save(cart);
        }
    }

    @Override
    public Integer getItemCountByUserId(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user ID: " + userId));
        return shoppingCartItemRepository.countByShoppingCartId(shoppingCart.getId());
    }
}