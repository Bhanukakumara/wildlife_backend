package com.example.wildlife_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductCategoryCreateDto {
    private String name;
    private String description;
    private String imageUrl;
    private boolean active;
}
