package com.example.wildlife_backend.dto.ProductCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCategoryCreateDto {
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must be less than 100 characters")
    private String name;

    private String description;

    @Size(max = 255, message = "Image URL must be at most 255 characters")
    private String imageUrl;

    private boolean active = true;

    private String createdBy;

    private String updatedBy;
}
