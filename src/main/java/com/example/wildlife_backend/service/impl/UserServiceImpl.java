package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.user.UserRegistrationDto;
import com.example.wildlife_backend.dto.user.UserProfileDto;
import com.example.wildlife_backend.dto.user.UserResponseDto;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.util.UserRole;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        if (!isEmailAvailable(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (!isUsernameAvailable(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        user.setIsActive(true);
        user.setIsSuspended(false);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Update allowed fields
        if (user.getFullName() != null) existingUser.setFullName(user.getFullName());
        if (user.getBio() != null) existingUser.setBio(user.getBio());
        if (user.getProfileImageUrl() != null) existingUser.setProfileImageUrl(user.getProfileImageUrl());
        if (user.getPhoneNumber() != null) existingUser.setPhoneNumber(user.getPhoneNumber());
        if (user.getWebsite() != null) existingUser.setWebsite(user.getWebsite());
        if (user.getLocation() != null) existingUser.setLocation(user.getLocation());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        user.setPassword(registrationDto.getPassword());
        user.setFullName(registrationDto.getFullName());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setBio(registrationDto.getBio());
        user.setProfileImageUrl(registrationDto.getProfileImageUrl());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setWebsite(registrationDto.getWebsite());
        user.setLocation(registrationDto.getLocation());

        User savedUser = createUser(user);
        return convertToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateProfile(Long userId, UserProfileDto profileDto) {
        User existingUser = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Update allowed fields
        if (profileDto.getEmail() != null) existingUser.setEmail(profileDto.getEmail());
        if (profileDto.getUsername() != null) existingUser.setUsername(profileDto.getUsername());
        if (profileDto.getFullName() != null) existingUser.setFullName(profileDto.getFullName());
        if (profileDto.getFirstName() != null) existingUser.setFirstName(profileDto.getFirstName());
        if (profileDto.getLastName() != null) existingUser.setLastName(profileDto.getLastName());
        if (profileDto.getBio() != null) existingUser.setBio(profileDto.getBio());
        if (profileDto.getProfileImageUrl() != null) existingUser.setProfileImageUrl(profileDto.getProfileImageUrl());
        if (profileDto.getPhoneNumber() != null) existingUser.setPhoneNumber(profileDto.getPhoneNumber());
        if (profileDto.getWebsite() != null) existingUser.setWebsite(profileDto.getWebsite());
        if (profileDto.getLocation() != null) existingUser.setLocation(profileDto.getLocation());

        User savedUser = userRepository.save(existingUser);
        return convertToUserResponseDto(savedUser);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void suspendUser(Long userId, String reason) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsSuspended(true);
        user.setSuspensionReason(reason);
        userRepository.save(user);
    }

    @Override
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsSuspended(false);
        user.setSuspensionReason(null);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalUserCount() {
        return userRepository.count();
    }

    @Override
    public void updateLastLogin(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserResponseById(Long id) {
        return getUserById(id).map(this::convertToUserResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUserResponses() {
        return getAllUsers().stream()
            .map(this::convertToUserResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUserResponsesByRole(UserRole role) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getActiveUserResponses() {
        return null;
    }

    private UserResponseDto convertToUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setBio(user.getBio());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setWebsite(user.getWebsite());
        dto.setLocation(user.getLocation());
        dto.setRoles(user.getRoles());
        dto.setEmailVerified(user.getEmailVerified());
        dto.setPhoneVerified(user.getPhoneVerified());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setIsActive(user.getIsActive());
        dto.setTotalEarnings(user.getTotalEarnings());
        dto.setTotalSales(user.getTotalSales());
        return dto;
    }
}
