package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.user.UserCreateDto;
import com.example.wildlife_backend.dto.user.UserGetDto;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.exception.DuplicateResourceException;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserGetDto createUser(UserCreateDto userCreateDto) {
        validateUserCreation(userCreateDto);

        User user = convertCreateDtoToUser(userCreateDto);
        User savedUser = userRepository.save(user);

        log.info("Created new user with ID: {}", savedUser.getId());
        return convertUserToGetDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserGetDto> getUserById(Long userId) {
        return Optional.ofNullable(userRepository.findById(userId)
                .map(this::convertUserToGetDto)
                .orElseThrow(() -> new ResourceNotFoundException("found with id: " + userId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserGetDto> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .map(this::convertUserToGetDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGetDto> getAllUsers() {
        List<User> all = userRepository.findAll();
        return all.stream().map(this::convertUserToGetDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<UserGetDto> updateUser(Long userId, UserCreateDto userDetails) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        validateUserUpdate(existingUser, userDetails);
        updateUserFromDto(existingUser, userDetails);
        User updatedUser = userRepository.save(existingUser);
        log.info("Updated user with ID: {}", userId);
        return Optional.of(convertUserToGetDto(updatedUser));
    }

    @Override
    @Transactional
    public boolean deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
        log.info("Deleted user with ID: {}", userId);
        return true;
    }



    private void validateUserCreation(UserCreateDto userCreateDto) {
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + userCreateDto.getEmail());
        }

        if (userRepository.existsByPhoneNumber(userCreateDto.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number already in use: " + userCreateDto.getPhoneNumber());
        }
        
        // Validate displayName
        if (userCreateDto.getDisplayName() == null || userCreateDto.getDisplayName().trim().isEmpty()) {
            // Generate display name from first name and last name if not provided
            String firstName = userCreateDto.getFirstName() != null ? userCreateDto.getFirstName().trim() : "";
            String lastName = userCreateDto.getLastName() != null ? userCreateDto.getLastName().trim() : "";
            userCreateDto.setDisplayName(firstName + " " + lastName);
        }
    }

    private void validateUserUpdate(User existingUser, UserCreateDto userDetails) {
        if (!existingUser.getEmail().equals(userDetails.getEmail()) &&
                userRepository.existsByEmail(userDetails.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + userDetails.getEmail());
        }

        if (!existingUser.getPhoneNumber().equals(userDetails.getPhoneNumber()) &&
                userRepository.existsByPhoneNumber(userDetails.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number already in use: " + userDetails.getPhoneNumber());
        }
        
        // Validate displayName
        if (userDetails.getDisplayName() == null || userDetails.getDisplayName().trim().isEmpty()) {
            // Generate display name from first name and last name if not provided
            String firstName = userDetails.getFirstName() != null ? userDetails.getFirstName().trim() : "";
            String lastName = userDetails.getLastName() != null ? userDetails.getLastName().trim() : "";
            userDetails.setDisplayName(firstName + " " + lastName);
        }
    }

    private User convertCreateDtoToUser(UserCreateDto userCreateDto) {
        return User.builder()
                .email(userCreateDto.getEmail())
                .firstName(userCreateDto.getFirstName())
                .middleName(userCreateDto.getMiddleName())
                .lastName(userCreateDto.getLastName())
                .displayName(userCreateDto.getDisplayName())
                .profilePicture(userCreateDto.getProfilePicture())
                .password(passwordEncoder.encode(userCreateDto.getPassword()))
                .phoneNumber(userCreateDto.getPhoneNumber())
                .dateOfBirth(userCreateDto.getDateOfBirth())
                .gender(userCreateDto.getGender())
                .role(userCreateDto.getRole())
                .accountStatus(userCreateDto.getAccountStatus())
                .build();
    }

    private UserGetDto convertUserToGetDto(User user) {
        return UserGetDto.builder()
                .id(user.getId()) // Corrected field name
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .displayName(user.getDisplayName())
                .profilePicture(user.getProfilePicture())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .role(user.getRole())
                .accountStatus(user.getAccountStatus())
                .createdDate(user.getCreatedAt())
                .updatedDate(user.getUpdatedAt())
                .deletedDate(user.getDeletedAt())
                .build();
    }

    private void updateUserFromDto(User user, UserCreateDto dto) {
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setFirstName(dto.getFirstName());
        user.setMiddleName(dto.getMiddleName());
        user.setLastName(dto.getLastName());
        user.setDisplayName(dto.getDisplayName());
        user.setProfilePicture(dto.getProfilePicture());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());
        user.setRole(dto.getRole());
        user.setAccountStatus(dto.getAccountStatus());
    }
}