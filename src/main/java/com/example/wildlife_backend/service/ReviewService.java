package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.review.ReviewCreateDto;
import com.example.wildlife_backend.dto.review.ReviewResponseDto;
import com.example.wildlife_backend.dto.review.ReviewUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    ReviewResponseDto createReview(ReviewCreateDto reviewDto);
    ReviewResponseDto updateReview(Long id, ReviewUpdateDto reviewDto);
    Optional<ReviewResponseDto> getReviewById(Long id);
    List<ReviewResponseDto> getAllReviews();
    void deleteReview(Long id);
    ReviewResponseDto approveReview(Long id);
    ReviewResponseDto rejectReview(Long id);
    List<ReviewResponseDto> getReviewsByPhoto(Long photoId);
    List<ReviewResponseDto> getReviewsByUser(Long userId);
    List<ReviewResponseDto> getApprovedReviews();
    List<ReviewResponseDto> getPendingReviews();
    void incrementHelpfulVotes(Long reviewId);
    Double getAverageRatingForPhoto(Long photoId);
    long getReviewCountForPhoto(Long photoId);
}
