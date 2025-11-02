package com.example.wildlife_backend.dto.photo;

import com.example.wildlife_backend.util.PhotoStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PhotoResponseDto {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String thumbnailUrl;
    private String previewUrl;
    private BigDecimal basePrice;
    private Boolean isPremium;
    private Boolean isFeatured;
    private PhotoStatus status;
    private String rejectionReason;
    private Integer downloadCount;
    private Integer viewCount;
    private Integer likeCount;
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
    private LocalDateTime capturedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime publishedAt;
    private Double averageRating;
    private Integer totalReviews;
    private Long userId;
    private String userUsername;
    private String userFullName;
    private Set<Long> categoryIds;
    private Set<String> categoryNames;
    private Set<String> tags;
}
