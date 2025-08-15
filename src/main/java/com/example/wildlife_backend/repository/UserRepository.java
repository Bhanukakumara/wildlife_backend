package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.util.AccountStatus;
import com.example.wildlife_backend.util.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phone);

    Optional<User> findByEmail(String email);

    List<User> findByAccountStatus(AccountStatus accountStatus);

    List<User> findByFirstNameContainingOrLastNameContainingIgnoreCase(String name, String lastName);

    List<User> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);

    List<User> findByRole(UserRole role);
}
