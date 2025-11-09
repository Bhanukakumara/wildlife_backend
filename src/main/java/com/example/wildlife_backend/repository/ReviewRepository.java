package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.Review;
import com.example.wildlife_backend.entity.Photo;
import com.example.wildlife_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUser(User user);

    List<Review> findByPhoto(Photo photo);

    List<Review> findByUserAndPhoto(User user, Photo photo);

    List<Review> findByIsApprovedTrue();

    List<Review> findByIsApprovedFalse();

    @Query("SELECT r FROM Review r WHERE r.photo.id = :photoId AND r.isApproved = true ORDER BY r.createdAt DESC")
    List<Review> findApprovedReviewsByPhotoId(@Param("photoId") Long photoId);

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.isApproved = true ORDER BY r.createdAt DESC")
    List<Review> findApprovedReviewsByUserId(@Param("userId") Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.photo.id = :photoId AND r.isApproved = true")
    Double getAverageRatingForPhoto(@Param("photoId") Long photoId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.photo.id = :photoId AND r.isApproved = true")
    long countApprovedReviewsForPhoto(@Param("photoId") Long photoId);

    @Query("SELECT r FROM Review r WHERE r.isApproved = true ORDER BY r.helpfulVotes DESC")
    List<Review> findMostHelpfulReviews();
}
