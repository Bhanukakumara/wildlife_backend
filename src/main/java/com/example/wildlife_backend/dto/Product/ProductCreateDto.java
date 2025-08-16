package com.example.wildlife_backend.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductCreateDto {
    private String name;
    private String description;
    private String primaryImageUrl;
    private boolean active;
    private boolean featured;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private Long categoryId;
}
