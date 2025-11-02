package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.Cart;
import com.example.wildlife_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

    Optional<Cart> findByUserId(Long userId);
}
