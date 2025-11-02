package com.example.wildlife_backend.entity;

import com.example.wildlife_backend.util.PhotoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "photos",
    indexes = {
        @Index(name = "idx_photo_user_id", columnList = "user_id"),
        @Index(name = "idx_photo_status", columnList = "status"),
        @Index(name = "idx_photo_featured", columnList = "is_featured"),
        @Index(name = "idx_photo_premium", columnList = "is_premium"),
        @Index(name = "idx_photo_created_at", columnList = "created_at"),
        @Index(name = "idx_photo_base_price", columnList = "base_price")
    }
)
public class Photo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotBlank
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "preview_url")
    private String previewUrl;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "is_premium")
    private Boolean isPremium = false;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PhotoStatus status = PhotoStatus.PENDING_REVIEW;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_format")
    private String fileFormat;

    @Column(name = "camera_model")
    private String cameraModel;

    @Column(name = "lens_info")
    private String lensInfo;

    @Column(name = "aperture")
    private String aperture;

    @Column(name = "shutter_speed")
    private String shutterSpeed;

    @Column(name = "iso")
    private Integer iso;

    @Column(name = "focal_length")
    private String focalLength;

    @Column(name = "location")
    private String location;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "captured_date")
    private LocalDateTime capturedDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
        name = "photo_categories",
        joinColumns = @JoinColumn(name = "photo_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Wishlist> wishlists = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "photo_tags", joinColumns = @JoinColumn(name = "photo_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = PhotoStatus.PENDING_REVIEW;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
