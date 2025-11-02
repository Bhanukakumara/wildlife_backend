package com.example.wildlife_backend.dto.category;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryUpdateDto {

    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Size(max = 120, message = "Slug must be less than 120 characters")
    private String slug;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Size(max = 255, message = "Image URL must be less than 255 characters")
    private String imageUrl;

    @Size(max = 255, message = "Icon URL must be less than 255 characters")
    private String iconUrl;

    private Integer displayOrder;

    private Boolean isFeatured;

    private Long parentId;
}
