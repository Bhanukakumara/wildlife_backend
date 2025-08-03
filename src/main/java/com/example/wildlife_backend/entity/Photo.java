package com.example.wildlife_backend.entity;

import com.example.wildlife_backend.util.Status;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long photoId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 255)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "caption", columnDefinition = "TEXT")
    private String caption;

    @Column(name = "keywords", columnDefinition = "TEXT")
    private String keywords;

    @Column(name = "location_taken", length = 255)
    private String locationTaken;

    @Column(name = "date_taken")
    private LocalDate dateTaken;

    @Column(name = "camera_make", length = 100)
    private String cameraMake;

    @Column(name = "camera_model", length = 100)
    private String cameraModel;

    @Column(name = "lens", length = 100)
    private String lens;

    @Column(name = "focal_length", length = 50)
    private String focalLength;

    @Column(name = "aperture", length = 50)
    private String aperture;

    @Column(name = "shutter_speed", length = 50)
    private String shutterSpeed;

    @Column(name = "iso", length = 50)
    private String iso;

    @Column(name = "original_filename", length = 255)
    private String originalFilename;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "image_width")
    private Integer imageWidth;

    @Column(name = "image_height")
    private Integer imageHeight;

    @Column(name = "color_profile", length = 100)
    private String colorProfile;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "featured", nullable = false)
    private Boolean featured = false;

    @Column(name = "views_count", nullable = false)
    private Long viewsCount = 0L;

    @Column(name = "downloads_count", nullable = false)
    private Long downloadsCount = 0L;

    @Column(name = "rating_average", nullable = false, precision = 3, scale = 2)
    private BigDecimal ratingAverage = BigDecimal.ZERO;

    @Column(name = "rating_count", nullable = false)
    private Integer ratingCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relationships
    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhotoReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "photo", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "photo", fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "photographer_id", nullable = false)
    private User photographer;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}

