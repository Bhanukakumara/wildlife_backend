package com.example.wildlife_backend.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "total_items")
    private Integer totalItems = 0;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.Set<CartItem> items = new java.util.HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addCartItem(CartItem item) {
        items.add(item);
        item.setCart(this);
        updateTotals();
    }

    public void removeCartItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
        updateTotals();
    }

    private void updateTotals() {
        totalItems = items.size();
        totalAmount = items.stream()
            .map(CartItem::getTotalPrice)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }
}
