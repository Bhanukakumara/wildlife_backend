package com.example.wildlife_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product Item name is required")
    @Size(max = 100, message = "Product Item name must be less than 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "sku", nullable = false, unique = true, length = 64)
    @NotBlank(message = "SKU is required")
    @Size(max = 64, message = "SKU must be at most 64 characters")
    private String sku;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have up to 2 decimal places")
    @NotNull(message = "Price is required")
    private BigDecimal price;

    @Column(name = "weight", precision = 8, scale = 2)
    private BigDecimal weight;

    @Column(name = "weight_unit", length = 10)
    @Size(max = 10, message = "Weight unit must be less than 10 characters")
    private String weightUnit;

    @Column(name = "length", precision = 8, scale = 2)
    private BigDecimal length;

    @Column(name = "width", precision = 8, scale = 2)
    private BigDecimal width;

    @Column(name = "height", precision = 8, scale = 2)
    private BigDecimal height;

    @Column(name = "is_customizable", nullable = false)
    private boolean customizable;

    @Column(name = "is_free_shipping", nullable = false)
    private boolean freeShipping;

    @Column(name = "qty_in_stock", nullable = false)
    @Min(value = 0, message = "Quantity cannot be negative")
    @NotNull(message = "Quantity in stock is required")
    private Integer qtyInStock;

    @Column(name = "image_url", length = 255)
    @Size(max = 255, message = "Image URL must be at most 255 characters")
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Product is required")
    private Product product;

    @OneToMany(mappedBy = "productItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ShoppingCartItem> shoppingCartItems = new HashSet<>();

    @OneToMany(mappedBy = "productItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrderLine> orderLines = new HashSet<>();
}
