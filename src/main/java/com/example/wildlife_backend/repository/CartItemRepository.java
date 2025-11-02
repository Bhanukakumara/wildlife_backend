package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.CartItem;
import com.example.wildlife_backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndId(Cart cart, Long id);

    void deleteByCart(Cart cart);
}
