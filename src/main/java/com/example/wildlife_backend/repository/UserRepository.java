package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.util.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE :roleName MEMBER OF u.roles")
    List<User> findByRoles_Name(@Param("roleName") UserRole roleName);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.isSuspended = false")
    List<User> findActiveUsers();

    @Query("SELECT u FROM User u WHERE u.roles IS NOT EMPTY")
    List<User> findUsersWithRoles();

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate")
    long countUsersRegisteredAfter(@Param("startDate") java.time.LocalDateTime startDate);
}
