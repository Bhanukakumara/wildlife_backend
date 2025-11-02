package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.photo.PhotoCreateDto;
import com.example.wildlife_backend.dto.photo.PhotoResponseDto;
import com.example.wildlife_backend.dto.photo.PhotoUpdateDto;
import com.example.wildlife_backend.entity.Photo;
import com.example.wildlife_backend.util.PhotoStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PhotoService {

    // CRUD operations
    PhotoResponseDto createPhoto(PhotoCreateDto photoDto);

    PhotoResponseDto updatePhoto(Long id, PhotoUpdateDto photoDto);

    Optional<PhotoResponseDto> getPhotoById(Long id);

    List<PhotoResponseDto> getAllPhotos();

    void deletePhoto(Long id);

    // Business logic methods
    PhotoResponseDto submitPhotoForReview(PhotoCreateDto photoDto);

    Photo submitPhotoForReview(Photo photo);

    PhotoResponseDto approvePhoto(Long id);

    PhotoResponseDto rejectPhoto(Long id, String reason);

    List<PhotoResponseDto> getPhotosByUser(Long userId);

    List<PhotoResponseDto> getPhotosByStatus(PhotoStatus status);

    List<PhotoResponseDto> getApprovedPhotos();

    List<PhotoResponseDto> getFeaturedPhotos();

    List<PhotoResponseDto> getPremiumPhotos();

    List<PhotoResponseDto> getPhotosByCategory(String categoryName);

    List<PhotoResponseDto> getPhotosByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    List<PhotoResponseDto> searchPhotos(String keyword);

    List<PhotoResponseDto> getPhotosByTag(String tag);

    void incrementViewCount(Long photoId);

    void incrementDownloadCount(Long photoId);

    // Statistics methods
    long getTotalPhotoCount();

    long getApprovedPhotoCount();

    long getPendingReviewCount();

    List<PhotoResponseDto> getMostViewedPhotos(int limit);

    List<PhotoResponseDto> getMostDownloadedPhotos(int limit);

    // Category management
    PhotoResponseDto addCategoryToPhoto(Long photoId, Long categoryId);

    PhotoResponseDto removeCategoryFromPhoto(Long photoId, Long categoryId);

    // Tag management
    PhotoResponseDto addTagToPhoto(Long photoId, String tag);

    PhotoResponseDto removeTagFromPhoto(Long photoId, String tag);
}
