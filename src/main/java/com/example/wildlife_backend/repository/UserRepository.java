package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.util.AccountStatus;
import com.example.wildlife_backend.util.Gender;
import com.example.wildlife_backend.util.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    
    @Query("SELECT u FROM User u WHERE " +
           "(:email IS NULL OR u.email LIKE %:email%) AND " +
           "(:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
           "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
           "(:phoneNumber IS NULL OR u.phoneNumber LIKE %:phoneNumber%) AND " +
           "(:dateOfBirth IS NULL OR u.dateOfBirth = :dateOfBirth) AND " +
           "(:gender IS NULL OR u.gender = :gender) AND " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:accountStatus IS NULL OR u.accountStatus = :accountStatus)")
    Page<User> searchUsers(@Param("email") String email,
                          @Param("firstName") String firstName,
                          @Param("lastName") String lastName,
                          @Param("phoneNumber") String phoneNumber,
                          @Param("dateOfBirth") LocalDate dateOfBirth,
                          @Param("gender") Gender gender,
                          @Param("role") UserRole role,
                          @Param("accountStatus") AccountStatus accountStatus,
                          Pageable pageable);
}
