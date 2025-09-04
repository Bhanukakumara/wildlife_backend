package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
    List<ShoppingCartItem> findByShoppingCartId(Long shoppingCartId);
    
    @Query("SELECT COUNT(sci) FROM ShoppingCartItem sci WHERE sci.shoppingCart.id = :cartId")
    int countByShoppingCartId(@Param("cartId") Long cartId);
    
    void deleteByShoppingCartId(Long shoppingCartId);
}
