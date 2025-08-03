package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.util.UserStatus;
import com.example.wildlife_backend.util.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByEmail(String email);

    List<User> findByUserStatus(UserStatus status);

    List<User> findByUserType(UserType userType);

    List<User> findByFirstNameContainingOrLastNameContainingIgnoreCase(String name, String lastName);

    List<User> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);
}
