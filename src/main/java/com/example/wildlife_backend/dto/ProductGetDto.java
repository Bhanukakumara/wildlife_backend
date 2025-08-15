package com.example.wildlife_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductGetDto {
    private Long id;
    private String name;
    private String description;
    private String primaryImageUrl;
    private boolean active;
    private boolean featured;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private ProductCategoryGetDto category;
    private List<ProductItemGetDto> productItems;
}
