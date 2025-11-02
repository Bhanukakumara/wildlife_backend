package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.review.ReviewCreateDto;
import com.example.wildlife_backend.dto.review.ReviewResponseDto;
import com.example.wildlife_backend.dto.review.ReviewUpdateDto;
import com.example.wildlife_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewCreateDto reviewDto) {
        ReviewResponseDto createdReview = reviewService.createReview(reviewDto);
        return ResponseEntity.ok(createdReview);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllApprovedReviews() {
        List<ReviewResponseDto> reviews = reviewService.getApprovedReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable Long id) {
        Optional<ReviewResponseDto> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByPhoto(@PathVariable Long photoId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByPhoto(photoId);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long id, @RequestBody ReviewUpdateDto reviewDto) {
        ReviewResponseDto updatedReview = reviewService.updateReview(id, reviewDto);
        return ResponseEntity.ok(updatedReview);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<ReviewResponseDto> approveReview(@PathVariable Long id) {
        ReviewResponseDto approvedReview = reviewService.approveReview(id);
        return ResponseEntity.ok(approvedReview);
    }

    @PostMapping("/{id}/helpful")
    public ResponseEntity<Void> markHelpful(@PathVariable Long id) {
        reviewService.incrementHelpfulVotes(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/pending")
    public ResponseEntity<List<ReviewResponseDto>> getPendingReviews() {
        List<ReviewResponseDto> pendingReviews = reviewService.getPendingReviews();
        return ResponseEntity.ok(pendingReviews);
    }

    @GetMapping("/photo/{photoId}/stats")
    public ResponseEntity<ReviewStats> getPhotoReviewStats(@PathVariable Long photoId) {
        Double averageRating = reviewService.getAverageRatingForPhoto(photoId);
        long reviewCount = reviewService.getReviewCountForPhoto(photoId);

        ReviewStats stats = new ReviewStats(averageRating, reviewCount);
        return ResponseEntity.ok(stats);
    }

    public static class ReviewStats {
        private Double averageRating;
        private long reviewCount;

        public ReviewStats(Double averageRating, long reviewCount) {
            this.averageRating = averageRating;
            this.reviewCount = reviewCount;
        }

        public Double getAverageRating() { return averageRating; }
        public long getReviewCount() { return reviewCount; }
    }
}
