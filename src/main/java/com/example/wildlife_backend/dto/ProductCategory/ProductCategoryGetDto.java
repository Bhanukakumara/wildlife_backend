package com.example.wildlife_backend.dto.ProductCategory;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class ProductCategoryGetDto {
    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

    private Set<Long> productIds = new HashSet<>();

    private Set<Long> promotionIds = new HashSet<>();
}
