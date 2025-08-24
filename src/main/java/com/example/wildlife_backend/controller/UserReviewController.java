package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.UserReview.UserReviewCreateDto;
import com.example.wildlife_backend.dto.UserReview.UserReviewGetDto;
import com.example.wildlife_backend.service.UserReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-reviews")
@CrossOrigin
@RequiredArgsConstructor
public class UserReviewController {
    private final UserReviewService userReviewService;

    // Create a new user review
    @PostMapping
    public ResponseEntity<UserReviewGetDto> createUserReview(@Valid @RequestBody UserReviewCreateDto userReviewCreateDto) {
        UserReviewGetDto createdReview = userReviewService.createUserReview(userReviewCreateDto);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    // Get user review by ID
    @GetMapping("/{userReviewId}")
    public ResponseEntity<UserReviewGetDto> getUserReviewById(@PathVariable Long userReviewId) {
        Optional<UserReviewGetDto> userReview = userReviewService.getUserReviewById(userReviewId);
        return userReview.map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all user reviews
    @GetMapping
    public ResponseEntity<List<UserReviewGetDto>> getAllUserReviews() {
        List<UserReviewGetDto> userReviews = userReviewService.getAllUserReviews();
        if (userReviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userReviews, HttpStatus.OK);
    }

    // Get user reviews by user ID
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<UserReviewGetDto>> getUserReviewsByUserId(@PathVariable Long userId) {
        List<UserReviewGetDto> userReviews = userReviewService.getUserReviewsByUserId(userId);
        if (userReviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userReviews, HttpStatus.OK);
    }

    // Get user reviews by order line ID
    @GetMapping("/by-order-line/{orderLineId}")
    public ResponseEntity<List<UserReviewGetDto>> getUserReviewsByOrderLineId(@PathVariable Long orderLineId) {
        List<UserReviewGetDto> userReviews = userReviewService.getUserReviewsByOrderLineId(orderLineId);
        if (userReviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userReviews, HttpStatus.OK);
    }

    // Get user reviews by rating range
    @GetMapping("/by-rating-range")
    public ResponseEntity<List<UserReviewGetDto>> getUserReviewsByRatingRange(
            @RequestParam Integer minRating,
            @RequestParam Integer maxRating) {
        List<UserReviewGetDto> userReviews = userReviewService.getUserReviewsByRatingRange(minRating, maxRating);
        if (userReviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userReviews, HttpStatus.OK);
    }

    // Update user review
    @PutMapping("/{userReviewId}")
    public ResponseEntity<UserReviewGetDto> updateUserReview(
            @PathVariable Long userReviewId,
            @Valid @RequestBody UserReviewCreateDto userReviewCreateDto) {
        Optional<UserReviewGetDto> updatedUserReview = userReviewService.updateUserReview(userReviewId, userReviewCreateDto);
        return updatedUserReview.map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete user review
    @DeleteMapping("/{userReviewId}")
    public ResponseEntity<Boolean> deleteUserReview(@PathVariable Long userReviewId) {
        boolean deleted = userReviewService.deleteUserReview(userReviewId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Bulk create user reviews
    @PostMapping("/bulk-create")
    public ResponseEntity<List<UserReviewGetDto>> bulkCreateUserReviews(
            @Valid @RequestBody List<UserReviewCreateDto> userReviewCreateDtos) {
        List<UserReviewGetDto> createdReviews = userReviewService.bulkCreateUserReviews(userReviewCreateDtos);
        return new ResponseEntity<>(createdReviews, HttpStatus.CREATED);
    }

    // Validate user review data
    @PostMapping("/validate")
    public ResponseEntity<String> validateUserReview(@Valid @RequestBody UserReviewCreateDto userReviewCreateDto) {
        boolean isValid = userReviewService.validateUserReview(userReviewCreateDto);
        if (isValid) {
            return new ResponseEntity<>("User review data is valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid user review data", HttpStatus.BAD_REQUEST);
    }

    // Get user reviews with order details
    @GetMapping("/with-order-details")
    public ResponseEntity<List<UserReviewGetDto>> getUserReviewsWithOrderDetails() {
        List<UserReviewGetDto> userReviews = userReviewService.getUserReviewsWithOrderDetails();
        if (userReviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userReviews, HttpStatus.OK);
    }
}