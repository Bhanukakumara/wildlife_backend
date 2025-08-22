package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.UserReview.UserReviewCreateDto;
import com.example.wildlife_backend.dto.UserReview.UserReviewGetDto;
import com.example.wildlife_backend.entity.OrderLine;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.entity.UserReview;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.OrderLineRepository;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.repository.UserReviewRepository;
import com.example.wildlife_backend.service.UserReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserReviewServiceImpl implements UserReviewService {
    private final UserReviewRepository userReviewRepository;
    private final UserRepository userRepository;
    private final OrderLineRepository orderLineRepository;

    @Override
    public UserReviewGetDto createUserReview(UserReviewCreateDto userReviewCreateDto) {
        UserReview userReview = new UserReview();
        
        // Set user
        if (userReviewCreateDto.getUserId() != null) {
            User user = userRepository.findById(userReviewCreateDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userReviewCreateDto.getUserId()));
            userReview.setUser(user);
        }
        
        // Set order line
        if (userReviewCreateDto.getOrderLineId() != null) {
            OrderLine orderLine = orderLineRepository.findById(userReviewCreateDto.getOrderLineId())
                    .orElseThrow(() -> new ResourceNotFoundException("OrderLine not found with id: " + userReviewCreateDto.getOrderLineId()));
            userReview.setOrderLine(orderLine);
        }
        
        userReview.setRating(userReviewCreateDto.getRating());
        userReview.setComment(userReviewCreateDto.getComment());
        
        UserReview savedUserReview = userReviewRepository.save(userReview);
        return convertToGetDto(savedUserReview);
    }

    @Override
    public Optional<UserReviewGetDto> getUserReviewById(Long userReviewId) {
        return userReviewRepository.findById(userReviewId)
                .map(this::convertToGetDto);
    }

    @Override
    public List<UserReviewGetDto> getAllUserReviews() {
        return userReviewRepository.findAll()
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserReviewGetDto> updateUserReview(Long userReviewId, UserReviewCreateDto userReviewCreateDto) {
        UserReview existingUserReview = userReviewRepository.findById(userReviewId)
                .orElseThrow(() -> new ResourceNotFoundException("UserReview not found with id: " + userReviewId));
        
        // Update user if provided
        if (userReviewCreateDto.getUserId() != null) {
            User user = userRepository.findById(userReviewCreateDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userReviewCreateDto.getUserId()));
            existingUserReview.setUser(user);
        }
        
        // Update order line if provided
        if (userReviewCreateDto.getOrderLineId() != null) {
            OrderLine orderLine = orderLineRepository.findById(userReviewCreateDto.getOrderLineId())
                    .orElseThrow(() -> new ResourceNotFoundException("OrderLine not found with id: " + userReviewCreateDto.getOrderLineId()));
            existingUserReview.setOrderLine(orderLine);
        }
        
        existingUserReview.setRating(userReviewCreateDto.getRating());
        existingUserReview.setComment(userReviewCreateDto.getComment());
        
        UserReview updatedUserReview = userReviewRepository.save(existingUserReview);
        return Optional.of(convertToGetDto(updatedUserReview));
    }

    @Override
    public void deleteUserReview(Long userReviewId) {
        if (!userReviewRepository.existsById(userReviewId)) {
            throw new ResourceNotFoundException("UserReview not found with id: " + userReviewId);
        }
        userReviewRepository.deleteById(userReviewId);
    }

    private UserReviewGetDto convertToGetDto(UserReview userReview) {
        return UserReviewGetDto.builder()
                .id(userReview.getId())
                .userId(userReview.getUser() != null ? userReview.getUser().getId() : null)
                .orderLineId(userReview.getOrderLine() != null ? userReview.getOrderLine().getId() : null)
                .rating(userReview.getRating())
                .comment(userReview.getComment())
                .build();
    }
}