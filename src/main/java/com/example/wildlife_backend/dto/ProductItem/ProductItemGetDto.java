package com.example.wildlife_backend.dto.ProductItem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProductItemGetDto {
    private Long id;

    private String name;

    private String sku;

    private String description;

    private BigDecimal price;

    private BigDecimal weight;

    private String weightUnit;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private boolean customizable;

    private boolean freeShipping;

    private Integer qtyInStock;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long productId;
}
