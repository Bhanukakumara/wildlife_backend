package com.example.wildlife_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reviews",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "photo_id"})
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Size(max = 1000)
    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "is_verified_purchase")
    private Boolean isVerifiedPurchase = false;

    @Column(name = "helpful_votes")
    private Integer helpfulVotes = 0;

    @Column(name = "total_votes")
    private Integer totalVotes = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_approved")
    private Boolean isApproved = true;

    @Column(name = "moderator_notes")
    private String moderatorNotes;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
