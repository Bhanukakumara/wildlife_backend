package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.UserReview.UserReviewCreateDto;
import com.example.wildlife_backend.dto.UserReview.UserReviewGetDto;

import java.util.List;
import java.util.Optional;

public interface UserReviewService {
    UserReviewGetDto createUserReview(UserReviewCreateDto userReviewCreateDto);
    Optional<UserReviewGetDto> getUserReviewById(Long userReviewId);
    List<UserReviewGetDto> getAllUserReviews();
    Optional<UserReviewGetDto> updateUserReview(Long userReviewId, UserReviewCreateDto userReviewCreateDto);
    void deleteUserReview(Long userReviewId);
}