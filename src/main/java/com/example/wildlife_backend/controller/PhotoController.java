package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.photo.PhotoCreateDto;
import com.example.wildlife_backend.dto.photo.PhotoResponseDto;
import com.example.wildlife_backend.dto.photo.PhotoUpdateDto;
import com.example.wildlife_backend.util.PhotoStatus;
import com.example.wildlife_backend.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    // Public browsing endpoints
    @GetMapping
    public ResponseEntity<List<PhotoResponseDto>> getAllApprovedPhotos() {
        List<PhotoResponseDto> photos = photoService.getApprovedPhotos();
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<PhotoResponseDto>> getFeaturedPhotos() {
        List<PhotoResponseDto> photos = photoService.getFeaturedPhotos();
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/premium")
    public ResponseEntity<List<PhotoResponseDto>> getPremiumPhotos() {
        List<PhotoResponseDto> photos = photoService.getPremiumPhotos();
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PhotoResponseDto>> searchPhotos(@RequestParam String keyword) {
        List<PhotoResponseDto> photos = photoService.searchPhotos(keyword);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<PhotoResponseDto>> getPhotosByCategory(@PathVariable String categoryName) {
        List<PhotoResponseDto> photos = photoService.getPhotosByCategory(categoryName);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<PhotoResponseDto>> getPhotosByTag(@PathVariable String tag) {
        List<PhotoResponseDto> photos = photoService.getPhotosByTag(tag);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<PhotoResponseDto>> getPhotosByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<PhotoResponseDto> photos = photoService.getPhotosByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/popular/viewed")
    public ResponseEntity<List<PhotoResponseDto>> getMostViewedPhotos(@RequestParam(defaultValue = "10") int limit) {
        List<PhotoResponseDto> photos = photoService.getMostViewedPhotos(limit);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/popular/downloaded")
    public ResponseEntity<List<PhotoResponseDto>> getMostDownloadedPhotos(@RequestParam(defaultValue = "10") int limit) {
        List<PhotoResponseDto> photos = photoService.getMostDownloadedPhotos(limit);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoResponseDto> getPhotoById(@PathVariable Long id) {
        Optional<PhotoResponseDto> photo = photoService.getPhotoById(id);
        if (photo.isPresent()) {
            photoService.incrementViewCount(id);
            return ResponseEntity.ok(photo.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Photographer endpoints
    @PostMapping
    @PreAuthorize("hasRole('PHOTOGRAPHER')")
    public ResponseEntity<PhotoResponseDto> createPhoto(@RequestBody PhotoCreateDto photoDto) {
        PhotoResponseDto createdPhoto = photoService.createPhoto(photoDto);
        return ResponseEntity.ok(createdPhoto);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('PHOTOGRAPHER')")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = photoService.uploadImage(file);
        return ResponseEntity.ok(imageUrl);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PHOTOGRAPHER')")
    public ResponseEntity<PhotoResponseDto> updatePhoto(@PathVariable Long id, @RequestBody PhotoUpdateDto photoDto) {
        PhotoResponseDto updatedPhoto = photoService.updatePhoto(id, photoDto);
        return ResponseEntity.ok(updatedPhoto);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('PHOTOGRAPHER') or hasRole('ADMIN')")
    public ResponseEntity<List<PhotoResponseDto>> getPhotosByUser(@PathVariable Long userId) {
        List<PhotoResponseDto> photos = photoService.getPhotosByUser(userId);
        return ResponseEntity.ok(photos);
    }

    // Admin endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<PhotoResponseDto>> getAllPhotos() {
        List<PhotoResponseDto> photos = photoService.getAllPhotos();
        return ResponseEntity.ok(photos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/status/{status}")
    public ResponseEntity<List<PhotoResponseDto>> getPhotosByStatus(@PathVariable PhotoStatus status) {
        List<PhotoResponseDto> photos = photoService.getPhotosByStatus(status);
        return ResponseEntity.ok(photos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<PhotoResponseDto> approvePhoto(@PathVariable Long id) {
        PhotoResponseDto approvedPhoto = photoService.approvePhoto(id);
        return ResponseEntity.ok(approvedPhoto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<PhotoResponseDto> rejectPhoto(@PathVariable Long id, @RequestParam String reason) {
        PhotoResponseDto rejectedPhoto = photoService.rejectPhoto(id, reason);
        return ResponseEntity.ok(rejectedPhoto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return ResponseEntity.ok().build();
    }

    // Category management
    @PreAuthorize("hasRole('PHOTOGRAPHER') or hasRole('ADMIN')")
    @PostMapping("/{photoId}/categories")
    public ResponseEntity<PhotoResponseDto> addCategoryToPhoto(@PathVariable Long photoId, @RequestParam Long categoryId) {
        PhotoResponseDto updatedPhoto = photoService.addCategoryToPhoto(photoId, categoryId);
        return ResponseEntity.ok(updatedPhoto);
    }

    @PreAuthorize("hasRole('PHOTOGRAPHER') or hasRole('ADMIN')")
    @DeleteMapping("/{photoId}/categories/{categoryId}")
    public ResponseEntity<PhotoResponseDto> removeCategoryFromPhoto(@PathVariable Long photoId, @PathVariable Long categoryId) {
        PhotoResponseDto updatedPhoto = photoService.removeCategoryFromPhoto(photoId, categoryId);
        return ResponseEntity.ok(updatedPhoto);
    }

    // Tag management
    @PreAuthorize("hasRole('PHOTOGRAPHER') or hasRole('ADMIN')")
    @PostMapping("/{photoId}/tags")
    public ResponseEntity<PhotoResponseDto> addTagToPhoto(@PathVariable Long photoId, @RequestParam String tag) {
        PhotoResponseDto updatedPhoto = photoService.addTagToPhoto(photoId, tag);
        return ResponseEntity.ok(updatedPhoto);
    }

    @PreAuthorize("hasRole('PHOTOGRAPHER') or hasRole('ADMIN')")
    @DeleteMapping("/{photoId}/tags")
    public ResponseEntity<PhotoResponseDto> removeTagFromPhoto(@PathVariable Long photoId, @RequestParam String tag) {
        PhotoResponseDto updatedPhoto = photoService.removeTagFromPhoto(photoId, tag);
        return ResponseEntity.ok(updatedPhoto);
    }

    // Statistics endpoints
    @GetMapping("/stats/count")
    public ResponseEntity<Long> getTotalPhotoCount() {
        long count = photoService.getTotalPhotoCount();
        return ResponseEntity.ok(count);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/approved")
    public ResponseEntity<Long> getApprovedPhotoCount() {
        long count = photoService.getApprovedPhotoCount();
        return ResponseEntity.ok(count);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/pending")
    public ResponseEntity<Long> getPendingReviewCount() {
        long count = photoService.getPendingReviewCount();
        return ResponseEntity.ok(count);
    }
}
