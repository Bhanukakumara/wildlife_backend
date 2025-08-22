package com.example.wildlife_backend.dto.UserReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserReviewCreateDto {
    private Long userId;
    private Long orderLineId;
    private Integer rating;
    private String comment;
}