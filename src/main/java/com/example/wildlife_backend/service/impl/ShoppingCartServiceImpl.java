package com.example.wildlife_backend.service.impl;

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

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final UserRepository userRepository;
    private final ProductItemRepository productItemRepository;

    @Override
    public ShoppingCart getCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));
                    ShoppingCart cart = new ShoppingCart();
                    cart.setUser(user);
                    return shoppingCartRepository.save(cart);
                });
    }

    @Override
    public ShoppingCart addItemToCart(Long userId, Long productItemId, int quantity) {
        ShoppingCart cart = getCartByUserId(userId);
        ProductItem productItem = productItemRepository.findById(productItemId)
                .orElseThrow(() -> new EntityNotFoundException("ProductItem not found"));

        Optional<ShoppingCartItem> existingItemOpt = cart.getShoppingCartItems().stream()
                .filter(item -> item.getProductItem().getId().equals(productItemId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            ShoppingCartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            shoppingCartItemRepository.save(existingItem);
        } else {
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
        ShoppingCart cart = getCartByUserId(userId);
        ShoppingCartItem item = shoppingCartItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        if (!item.getShoppingCart().getId().equals(cart.getId())) {
            throw new IllegalStateException("Item does not belong to user's cart");
        }

        cart.getShoppingCartItems().remove(item);
        shoppingCartItemRepository.delete(item);

        return shoppingCartRepository.save(cart);
    }

    @Override
    public Set<ShoppingCartItem> getItemsInCart(Long userId) {
        return getCartByUserId(userId).getShoppingCartItems();
    }

    @Override
    public void clearCart(Long userId) {
        ShoppingCart cart = getCartByUserId(userId);
        shoppingCartItemRepository.deleteAll(cart.getShoppingCartItems());
        cart.getShoppingCartItems().clear();
        shoppingCartRepository.save(cart);
    }
}
