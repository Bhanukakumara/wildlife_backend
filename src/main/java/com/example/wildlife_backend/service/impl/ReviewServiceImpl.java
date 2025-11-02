package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.review.ReviewCreateDto;
import com.example.wildlife_backend.dto.review.ReviewResponseDto;
import com.example.wildlife_backend.dto.review.ReviewUpdateDto;
import com.example.wildlife_backend.entity.Review;
import com.example.wildlife_backend.entity.Photo;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.repository.ReviewRepository;
import com.example.wildlife_backend.repository.PhotoRepository;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    @Override
    public ReviewResponseDto createReview(ReviewCreateDto reviewDto) {
        User user = userRepository.findById(reviewDto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Photo photo = photoRepository.findById(reviewDto.getPhotoId())
            .orElseThrow(() -> new RuntimeException("Photo not found"));

        Review review = new Review();
        review.setUser(user);
        review.setPhoto(photo);
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());

        Review savedReview = createReviewEntity(review);
        return convertToReviewResponseDto(savedReview);
    }

    @Override
    public ReviewResponseDto updateReview(Long id, ReviewUpdateDto reviewDto) {
        Review existingReview = reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found"));

        if (reviewDto.getRating() != null) existingReview.setRating(reviewDto.getRating());
        if (reviewDto.getComment() != null) existingReview.setComment(reviewDto.getComment());

        Review savedReview = reviewRepository.save(existingReview);
        return convertToReviewResponseDto(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewResponseDto> getReviewById(Long id) {
        return reviewRepository.findById(id)
            .map(this::convertToReviewResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
            .map(this::convertToReviewResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByPhoto(Long photoId) {
        return reviewRepository.findApprovedReviewsByPhotoId(photoId).stream()
            .map(this::convertToReviewResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByUser(Long userId) {
        return reviewRepository.findApprovedReviewsByUserId(userId).stream()
            .map(this::convertToReviewResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getApprovedReviews() {
        return reviewRepository.findByIsApprovedTrue().stream()
            .map(this::convertToReviewResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getPendingReviews() {
        return reviewRepository.findByIsApprovedFalse().stream()
            .map(this::convertToReviewResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    public ReviewResponseDto approveReview(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setIsApproved(true);
        Review savedReview = reviewRepository.save(review);
        return convertToReviewResponseDto(savedReview);
    }

    @Override
    public ReviewResponseDto rejectReview(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setIsApproved(false);
        Review savedReview = reviewRepository.save(review);
        return convertToReviewResponseDto(savedReview);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public void incrementHelpfulVotes(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setHelpfulVotes(review.getHelpfulVotes() + 1);
        reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRatingForPhoto(Long photoId) {
        return reviewRepository.getAverageRatingForPhoto(photoId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getReviewCountForPhoto(Long photoId) {
        return reviewRepository.countApprovedReviewsForPhoto(photoId);
    }

    private Review createReviewEntity(Review review) {
        review.setIsApproved(false);
        return reviewRepository.save(review);
    }

    private ReviewResponseDto convertToReviewResponseDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserUsername(review.getUser().getUsername());
        dto.setUserFullName(review.getUser().getFullName());
        dto.setPhotoId(review.getPhoto().getId());
        dto.setPhotoTitle(review.getPhoto().getTitle());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setIsVerifiedPurchase(review.getIsVerifiedPurchase());
        dto.setHelpfulVotes(review.getHelpfulVotes());
        dto.setTotalVotes(review.getTotalVotes());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        dto.setIsApproved(review.getIsApproved());
        dto.setModeratorNotes(review.getModeratorNotes());
        return dto;
    }
}
