package com.example.wildlife_backend.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponseDto {

    private Long id;
    private Long userId;
    private String userUsername;
    private String userFullName;
    private Long photoId;
    private String photoTitle;
    private Integer rating;
    private String comment;
    private Boolean isVerifiedPurchase;
    private Integer helpfulVotes;
    private Integer totalVotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isApproved;
    private String moderatorNotes;
}
