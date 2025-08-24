package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.UserReview.UserReviewCreateDto;
import com.example.wildlife_backend.dto.UserReview.UserReviewGetDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface UserReviewService {
    UserReviewGetDto createUserReview(UserReviewCreateDto userReviewCreateDto);
    Optional<UserReviewGetDto> getUserReviewById(Long userReviewId);
    List<UserReviewGetDto> getAllUserReviews();
    Optional<UserReviewGetDto> updateUserReview(Long userReviewId, UserReviewCreateDto userReviewCreateDto);
    boolean deleteUserReview(Long userReviewId);

    List<UserReviewGetDto> getUserReviewsByUserId(Long userId);

    List<UserReviewGetDto> getUserReviewsByOrderLineId(Long orderLineId);

    List<UserReviewGetDto> getUserReviewsByRatingRange(Integer minRating, Integer maxRating);

    List<UserReviewGetDto> bulkCreateUserReviews(@Valid List<UserReviewCreateDto> userReviewCreateDtos);

    boolean validateUserReview(@Valid UserReviewCreateDto userReviewCreateDto);

    List<UserReviewGetDto> getUserReviewsWithOrderDetails();
}