package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.Photo;
import com.example.wildlife_backend.util.PhotoStatus;
import com.example.wildlife_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findByUser(User user);

    List<Photo> findByStatus(PhotoStatus status);

    List<Photo> findByIsFeaturedTrue();

    List<Photo> findByIsPremiumTrue();

    List<Photo> findByCategories_Name(String categoryName);

    @Query("SELECT p FROM Photo p WHERE p.status = 'APPROVED' ORDER BY p.createdAt DESC")
    List<Photo> findApprovedPhotosOrderByCreatedAtDesc();

    @Query("SELECT p FROM Photo p WHERE p.status = 'APPROVED' AND p.isFeatured = true ORDER BY p.createdAt DESC")
    List<Photo> findFeaturedPhotos();

    @Query("SELECT p FROM Photo p WHERE p.status = 'APPROVED' AND p.basePrice BETWEEN :minPrice AND :maxPrice")
    List<Photo> findPhotosByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT p FROM Photo p WHERE p.status = 'APPROVED' AND LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Photo> searchPhotosByTitle(@Param("keyword") String keyword);

    @Query("SELECT p FROM Photo p WHERE p.status = 'APPROVED' AND :tag MEMBER OF p.tags")
    List<Photo> findPhotosByTag(@Param("tag") String tag);

    @Query("SELECT p FROM Photo p WHERE p.user.id = :userId AND p.status = 'APPROVED'")
    List<Photo> findApprovedPhotosByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Photo p WHERE p.status = 'APPROVED'")
    long countApprovedPhotos();

    @Query("SELECT COUNT(p) FROM Photo p WHERE p.status = 'PENDING_REVIEW'")
    long countPendingReviewPhotos();

    @Query("SELECT p FROM Photo p WHERE p.status = 'APPROVED' ORDER BY p.viewCount DESC")
    List<Photo> findMostViewedPhotos();

    @Query("SELECT p FROM Photo p WHERE p.status = 'APPROVED' ORDER BY p.downloadCount DESC")
    List<Photo> findMostDownloadedPhotos();

    Optional<Photo> findByIdAndStatus(Long id, PhotoStatus status);
}
