package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.photo.PhotoCreateDto;
import com.example.wildlife_backend.dto.photo.PhotoResponseDto;
import com.example.wildlife_backend.dto.photo.PhotoUpdateDto;
import com.example.wildlife_backend.entity.Photo;
import com.example.wildlife_backend.util.PhotoStatus;
import com.example.wildlife_backend.entity.Category;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.repository.PhotoRepository;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.repository.CategoryRepository;
import com.example.wildlife_backend.service.PhotoService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final Cloudinary cloudinary;

    @Override
    public PhotoResponseDto createPhoto(PhotoCreateDto photoDto) {
        User user = userRepository.findById(photoDto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Photo photo = new Photo();
        photo.setUser(user);
        photo.setTitle(photoDto.getTitle());
        photo.setDescription(photoDto.getDescription());
        photo.setImageUrl(photoDto.getImageUrl());
        photo.setThumbnailUrl(photoDto.getThumbnailUrl());
        photo.setPreviewUrl(photoDto.getPreviewUrl());
        photo.setBasePrice(photoDto.getBasePrice());
        photo.setIsPremium(photoDto.getIsPremium());
        photo.setIsFeatured(photoDto.getIsFeatured());

        // Technical metadata
        photo.setWidth(photoDto.getWidth());
        photo.setHeight(photoDto.getHeight());
        photo.setFileSize(photoDto.getFileSize());
        photo.setFileFormat(photoDto.getFileFormat());
        photo.setCameraModel(photoDto.getCameraModel());
        photo.setLensInfo(photoDto.getLensInfo());
        photo.setAperture(photoDto.getAperture());
        photo.setShutterSpeed(photoDto.getShutterSpeed());
        photo.setIso(photoDto.getIso());
        photo.setFocalLength(photoDto.getFocalLength());
        photo.setLocation(photoDto.getLocation());
        photo.setLatitude(photoDto.getLatitude());
        photo.setLongitude(photoDto.getLongitude());

        // Set tags and categories
        if (photoDto.getTags() != null) {
            photo.getTags().addAll(photoDto.getTags());
        }
        if (photoDto.getCategoryIds() != null) {
            Set<Category> categories = photoDto.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId)))
                .collect(Collectors.toSet());
            photo.getCategories().addAll(categories);
        }

        Photo savedPhoto = createPhotoEntity(photo);
        return convertToPhotoResponseDto(savedPhoto);
    }

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public PhotoResponseDto updatePhoto(Long id, PhotoUpdateDto photoDto) {
        Photo existingPhoto = photoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Photo not found"));

        // Update allowed fields
        if (photoDto.getTitle() != null) existingPhoto.setTitle(photoDto.getTitle());
        if (photoDto.getDescription() != null) existingPhoto.setDescription(photoDto.getDescription());
        if (photoDto.getImageUrl() != null) existingPhoto.setImageUrl(photoDto.getImageUrl());
        if (photoDto.getThumbnailUrl() != null) existingPhoto.setThumbnailUrl(photoDto.getThumbnailUrl());
        if (photoDto.getPreviewUrl() != null) existingPhoto.setPreviewUrl(photoDto.getPreviewUrl());
        if (photoDto.getBasePrice() != null) existingPhoto.setBasePrice(photoDto.getBasePrice());
        if (photoDto.getIsPremium() != null) existingPhoto.setIsPremium(photoDto.getIsPremium());
        if (photoDto.getIsFeatured() != null) existingPhoto.setIsFeatured(photoDto.getIsFeatured());

        // Technical metadata
        if (photoDto.getWidth() != null) existingPhoto.setWidth(photoDto.getWidth());
        if (photoDto.getHeight() != null) existingPhoto.setHeight(photoDto.getHeight());
        if (photoDto.getFileSize() != null) existingPhoto.setFileSize(photoDto.getFileSize());
        if (photoDto.getFileFormat() != null) existingPhoto.setFileFormat(photoDto.getFileFormat());
        if (photoDto.getCameraModel() != null) existingPhoto.setCameraModel(photoDto.getCameraModel());
        if (photoDto.getLensInfo() != null) existingPhoto.setLensInfo(photoDto.getLensInfo());
        if (photoDto.getAperture() != null) existingPhoto.setAperture(photoDto.getAperture());
        if (photoDto.getShutterSpeed() != null) existingPhoto.setShutterSpeed(photoDto.getShutterSpeed());
        if (photoDto.getIso() != null) existingPhoto.setIso(photoDto.getIso());
        if (photoDto.getFocalLength() != null) existingPhoto.setFocalLength(photoDto.getFocalLength());
        if (photoDto.getLocation() != null) existingPhoto.setLocation(photoDto.getLocation());
        if (photoDto.getLatitude() != null) existingPhoto.setLatitude(photoDto.getLatitude());
        if (photoDto.getLongitude() != null) existingPhoto.setLongitude(photoDto.getLongitude());

        // Update tags and categories
        if (photoDto.getTags() != null) {
            existingPhoto.getTags().clear();
            existingPhoto.getTags().addAll(photoDto.getTags());
        }
        if (photoDto.getCategoryIds() != null) {
            existingPhoto.getCategories().clear();
            Set<Category> categories = photoDto.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId)))
                .collect(Collectors.toSet());
            existingPhoto.getCategories().addAll(categories);
        }

        Photo savedPhoto = photoRepository.save(existingPhoto);
        return convertToPhotoResponseDto(savedPhoto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PhotoResponseDto> getPhotoById(Long id) {
        return photoRepository.findById(id)
            .map(this::convertToPhotoResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> getAllPhotos() {
        return photoRepository.findAll().stream()
            .map(this::convertToPhotoResponseDto)
            .collect(Collectors.toList());
    }

    // Add conversion and helper methods at the end
    private Photo createPhotoEntity(Photo photo) {
        photo.setStatus(PhotoStatus.PENDING_REVIEW);
        photo.setViewCount(0);
        photo.setDownloadCount(0);
        photo.setLikeCount(0);
        photo.setAverageRating(0.0);
        photo.setTotalReviews(0);
        return photoRepository.save(photo);
    }

    private PhotoResponseDto convertToPhotoResponseDto(Photo photo) {
        PhotoResponseDto dto = new PhotoResponseDto();
        dto.setId(photo.getId());
        dto.setTitle(photo.getTitle());
        dto.setDescription(photo.getDescription());
        dto.setImageUrl(photo.getImageUrl());
        dto.setThumbnailUrl(photo.getThumbnailUrl());
        dto.setPreviewUrl(photo.getPreviewUrl());
        dto.setBasePrice(photo.getBasePrice());
        dto.setIsPremium(photo.getIsPremium());
        dto.setIsFeatured(photo.getIsFeatured());
        dto.setStatus(photo.getStatus());
        dto.setRejectionReason(photo.getRejectionReason());
        dto.setDownloadCount(photo.getDownloadCount());
        dto.setViewCount(photo.getViewCount());
        dto.setLikeCount(photo.getLikeCount());
        dto.setWidth(photo.getWidth());
        dto.setHeight(photo.getHeight());
        dto.setFileSize(photo.getFileSize());
        dto.setFileFormat(photo.getFileFormat());
        dto.setCameraModel(photo.getCameraModel());
        dto.setLensInfo(photo.getLensInfo());
        dto.setAperture(photo.getAperture());
        dto.setShutterSpeed(photo.getShutterSpeed());
        dto.setIso(photo.getIso());
        dto.setFocalLength(photo.getFocalLength());
        dto.setLocation(photo.getLocation());
        dto.setLatitude(photo.getLatitude());
        dto.setLongitude(photo.getLongitude());
        dto.setCapturedDate(photo.getCapturedDate());
        dto.setCreatedAt(photo.getCreatedAt());
        dto.setUpdatedAt(photo.getUpdatedAt());
        dto.setApprovedAt(photo.getApprovedAt());
        dto.setPublishedAt(photo.getPublishedAt());
        dto.setAverageRating(photo.getAverageRating());
        dto.setTotalReviews(photo.getTotalReviews());
        dto.setUserId(photo.getUser().getId());
        dto.setUserUsername(photo.getUser().getUsername());
        dto.setUserFullName(photo.getUser().getFullName());
        dto.setCategoryIds(photo.getCategories().stream().map(Category::getId).collect(Collectors.toSet()));
        dto.setCategoryNames(photo.getCategories().stream().map(Category::getName).collect(Collectors.toSet()));
        dto.setTags(photo.getTags());
        return dto;
    }

    @Override
    public void deletePhoto(Long id) {
        photoRepository.deleteById(id);
    }

    @Override
    public PhotoResponseDto submitPhotoForReview(PhotoCreateDto photoDto) {
        return null;
    }

    @Override
    public Photo submitPhotoForReview(Photo photo) {
        photo.setStatus(PhotoStatus.PENDING_REVIEW);
        return photoRepository.save(photo);
    }

    @Override
    public PhotoResponseDto approvePhoto(Long id) {
        Photo photo = photoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Photo not found"));

        photo.setStatus(PhotoStatus.APPROVED);
        photo.setApprovedAt(LocalDateTime.now());
        photo.setPublishedAt(LocalDateTime.now());

        Photo savedPhoto = photoRepository.save(photo);
        return convertToPhotoResponseDto(savedPhoto);
    }

    @Override
    public PhotoResponseDto rejectPhoto(Long id, String reason) {
        Photo photo = photoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Photo not found"));

        photo.setStatus(PhotoStatus.REJECTED);
        photo.setRejectionReason(reason);

        Photo savedPhoto = photoRepository.save(photo);
        return convertToPhotoResponseDto(savedPhoto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> getPhotosByUser(Long userId) {
        List<Photo> photos = photoRepository.findApprovedPhotosByUserId(userId);
        return photos.stream()
                .map(this::convertToPhotoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> getPhotosByStatus(PhotoStatus status) {
        List<Photo> photos = photoRepository.findByStatus(status);
        return photos.stream()
                .map(this::convertToPhotoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> getApprovedPhotos() {
        List<Photo> photos = photoRepository.findApprovedPhotosOrderByCreatedAtDesc();
        return photos.stream()
                .map(this::convertToPhotoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> getFeaturedPhotos() {
        List<Photo> photos = photoRepository.findFeaturedPhotos();
        return photos.stream()
                .map(this::convertToPhotoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> getPremiumPhotos() {
        List<Photo> photos = photoRepository.findByIsPremiumTrue();
        return photos.stream()
                .map(this::convertToPhotoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> getPhotosByCategory(String categoryName) {
        List<Photo> photos = photoRepository.findByCategories_Name(categoryName);
        return photos.stream()
                .map(this::convertToPhotoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> getPhotosByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Photo> photos = photoRepository.findPhotosByPriceRange(minPrice, maxPrice);
        return photos.stream()
                .map(this::convertToPhotoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> searchPhotos(String keyword) {
        List<Photo> photos = photoRepository.searchPhotosByTitle(keyword);
        return photos.stream()
                .map(this::convertToPhotoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> getPhotosByTag(String tag) {
        List<Photo> photos = photoRepository.findPhotosByTag(tag);
        return photos.stream()
                .map(this::convertToPhotoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementViewCount(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Photo not found"));
        photo.setViewCount(photo.getViewCount() + 1);
        photoRepository.save(photo);
    }

    @Override
    public void incrementDownloadCount(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Photo not found"));
        photo.setDownloadCount(photo.getDownloadCount() + 1);
        photoRepository.save(photo);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPhotoCount() {
        return photoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getApprovedPhotoCount() {
        return photoRepository.countApprovedPhotos();
    }

    @Override
    @Transactional(readOnly = true)
    public long getPendingReviewCount() {
        return photoRepository.countPendingReviewPhotos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> getMostViewedPhotos(int limit) {
        List<Photo> photos = photoRepository.findMostViewedPhotos();
        return photos.subList(0, Math.min(limit, photos.size()))
                .stream()
                .map(this::convertToPhotoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDto> getMostDownloadedPhotos(int limit) {
        List<Photo> photos = photoRepository.findMostDownloadedPhotos();
        return photos.subList(0, Math.min(limit, photos.size()))
                .stream()
                .map(this::convertToPhotoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PhotoResponseDto addCategoryToPhoto(Long photoId, Long categoryId) {
        Photo photo = photoRepository.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Photo not found"));

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        if (photo.getCategories() == null) {
            photo.setCategories(new HashSet<>());
        }

        photo.getCategories().add(category);
        Photo savedPhoto = photoRepository.save(photo);
        return convertToPhotoResponseDto(savedPhoto);
    }

    @Override
    public PhotoResponseDto removeCategoryFromPhoto(Long photoId, Long categoryId) {
        Photo photo = photoRepository.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Photo not found"));

        photo.getCategories().removeIf(category -> category.getId().equals(categoryId));
        Photo savedPhoto = photoRepository.save(photo);
        return convertToPhotoResponseDto(savedPhoto);
    }

    @Override
    public PhotoResponseDto addTagToPhoto(Long photoId, String tag) {
        Photo photo = photoRepository.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Photo not found"));

        if (photo.getTags() == null) {
            photo.setTags(new HashSet<>());
        }

        photo.getTags().add(tag);
        Photo savedPhoto = photoRepository.save(photo);
        return convertToPhotoResponseDto(savedPhoto);
    }

    @Override
    public PhotoResponseDto removeTagFromPhoto(Long photoId, String tag) {
        Photo photo = photoRepository.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Photo not found"));

        if (photo.getTags() != null) {
            photo.getTags().remove(tag);
        }

        Photo savedPhoto = photoRepository.save(photo);
        return convertToPhotoResponseDto(savedPhoto);
    }
}
