package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.Wishlist;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUser(User user);

    List<Wishlist> findByPhoto(Photo photo);

    Optional<Wishlist> findByUserAndPhoto(User user, Photo photo);

    boolean existsByUserAndPhoto(User user, Photo photo);
}
