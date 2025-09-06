package com.example.wildlife_backend.dto.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateDto {
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must be less than 100 characters")
    private String name;

    private String description;

    private String primaryImageUrl;

    private boolean active = true;

    private boolean featured = false;

    @Size(max = 255, message = "Meta title must be at most 255 characters")
    private String metaTitle;

    private String metaDescription;

    private String metaKeywords;

    private String createdBy;

    private String updatedBy;

    @NotNull(message = "Product category is required")
    private Long categoryId;
}
