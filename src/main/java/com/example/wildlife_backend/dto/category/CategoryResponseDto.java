package com.example.wildlife_backend.dto.category;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CategoryResponseDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private String iconUrl;
    private Integer displayOrder;
    private Boolean isFeatured;
    private Boolean isActive;
    private Integer photoCount;
    private Long parentId;
    private String parentName;
    private Set<Long> childrenIds;
    private Set<String> childrenNames;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
