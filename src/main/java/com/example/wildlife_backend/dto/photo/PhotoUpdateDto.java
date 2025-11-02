package com.example.wildlife_backend.dto.photo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class PhotoUpdateDto {

    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    private String imageUrl;

    private String thumbnailUrl;

    private String previewUrl;

    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    private BigDecimal basePrice;

    private Boolean isPremium;

    private Boolean isFeatured;

    private Integer width;

    private Integer height;

    private Long fileSize;

    private String fileFormat;

    private String cameraModel;

    private String lensInfo;

    private String aperture;

    private String shutterSpeed;

    private Integer iso;

    private String focalLength;

    private String location;

    private Double latitude;

    private Double longitude;

    private Set<String> tags;

    private Set<Long> categoryIds;
}
