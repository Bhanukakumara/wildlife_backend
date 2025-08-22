package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.UserReview.UserReviewCreateDto;
import com.example.wildlife_backend.dto.UserReview.UserReviewGetDto;
import com.example.wildlife_backend.service.UserReviewService;
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
    public ResponseEntity<UserReviewGetDto> createUserReview(@RequestBody UserReviewCreateDto userReviewCreateDto) {
        return new ResponseEntity<>(userReviewService.createUserReview(userReviewCreateDto), HttpStatus.CREATED);
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

    // Update user review
    @PutMapping("/{userReviewId}")
    public ResponseEntity<UserReviewGetDto> updateUserReview(@PathVariable Long userReviewId, @RequestBody UserReviewCreateDto userReviewCreateDto) {
        Optional<UserReviewGetDto> updatedUserReview = userReviewService.updateUserReview(userReviewId, userReviewCreateDto);
        return updatedUserReview.map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete user review
    @DeleteMapping("/{userReviewId}")
    public ResponseEntity<Void> deleteUserReview(@PathVariable Long userReviewId) {
        try {
            userReviewService.deleteUserReview(userReviewId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}