package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.user.UserRegistrationDto;
import com.example.wildlife_backend.dto.user.UserProfileDto;
import com.example.wildlife_backend.dto.user.UserResponseDto;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.util.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Basic CRUD operations
    User createUser(User user);

    User updateUser(Long id, User user);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);

    List<User> getAllUsers();

    void deleteUser(Long id);

    // Business logic methods
    UserResponseDto registerUser(UserRegistrationDto registrationDto);

    UserResponseDto updateProfile(Long userId, UserProfileDto profileDto);

    void changePassword(Long userId, String oldPassword, String newPassword);

    void suspendUser(Long userId, String reason);

    void activateUser(Long userId);

    boolean isEmailAvailable(String email);

    boolean isUsernameAvailable(String username);

    Optional<UserResponseDto> getUserResponseById(Long id);

    List<UserResponseDto> getAllUserResponses();

    List<UserResponseDto> getUserResponsesByRole(UserRole role);

    List<UserResponseDto> getActiveUserResponses();

    long getTotalUserCount();

    void updateLastLogin(Long userId);
}
