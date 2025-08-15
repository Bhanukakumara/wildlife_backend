package com.example.wildlife_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "order_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_id", nullable = false)
    @NotNull(message = "Item is required")
    private ProductItem productItem;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull(message = "Order is required")
    private ShopOrder shopOrder;

    private Long quantity;
    private BigDecimal price;

    @OneToMany(mappedBy = "orderLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserReview> userReviews = new HashSet<>();
}
