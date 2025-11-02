package com.example.wildlife_backend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "categories",
    uniqueConstraints = @UniqueConstraint(columnNames = "slug"),
    indexes = {
        @Index(name = "idx_category_name", columnList = "name"),
        @Index(name = "idx_category_slug", columnList = "slug"),
        @Index(name = "idx_category_parent_id", columnList = "parent_id"),
        @Index(name = "idx_category_featured", columnList = "is_featured"),
        @Index(name = "idx_category_created_at", columnList = "created_at")
    }
)
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true)
    private String name;

    @NotBlank
    @Size(max = 120)
    @Column(unique = true)
    private String slug;

    @Size(max = 500)
    private String description;

    @Size(max = 255)
    private String imageUrl;

    @Size(max = 255)
    private String iconUrl;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "photo_count")
    private Integer photoCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> children = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "categories")
    private Set<Photo> photos = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (slug == null && name != null) {
            slug = generateSlug(name);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
            .replaceAll("[^a-z0-9\\s]", "")
            .replaceAll("\\s+", "-")
            .replaceAll("-+", "-")
            .replaceAll("^-|-", "");
    }
}
