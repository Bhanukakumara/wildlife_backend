package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.user.UserCreateDto;
import com.example.wildlife_backend.dto.user.UserGetDto;
import com.example.wildlife_backend.dto.user.UserSearchDto;
import com.example.wildlife_backend.dto.user.UserUpdatePasswordDto;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.exception.DuplicateResourceException;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
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
    @Transactional(readOnly = true)
    public Page<UserGetDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertUserToGetDto);
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
    @Transactional(readOnly = true)
    public Page<UserGetDto> searchUsers(UserSearchDto searchDto, Pageable pageable) {
        Page<User> users = userRepository.searchUsers(
            searchDto.getEmail(),
            searchDto.getFirstName(),
            searchDto.getLastName(),
            searchDto.getPhoneNumber(),
            searchDto.getDateOfBirth(),
            searchDto.getGender(),
            searchDto.getRole(),
            searchDto.getAccountStatus(),
            pageable
        );
        return users.map(this::convertUserToGetDto);
    }
    
    @Override
    @Transactional
    public Optional<UserGetDto> partialUpdateUser(Long userId, UserCreateDto userDetails) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Only update fields that are provided in the DTO
        if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
            validateUserUpdate(existingUser, userDetails);
            existingUser.setEmail(userDetails.getEmail());
        }
        
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        if (userDetails.getFirstName() != null) {
            existingUser.setFirstName(userDetails.getFirstName());
        }
        
        if (userDetails.getMiddleName() != null) {
            existingUser.setMiddleName(userDetails.getMiddleName());
        }
        
        if (userDetails.getLastName() != null) {
            existingUser.setLastName(userDetails.getLastName());
        }
        
        if (userDetails.getDisplayName() != null) {
            existingUser.setDisplayName(userDetails.getDisplayName());
        }
        
        if (userDetails.getProfilePicture() != null) {
            existingUser.setProfilePicture(userDetails.getProfilePicture());
        }
        
        if (userDetails.getPhoneNumber() != null && !userDetails.getPhoneNumber().isEmpty()) {
            existingUser.setPhoneNumber(userDetails.getPhoneNumber());
        }
        
        if (userDetails.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(userDetails.getDateOfBirth());
        }
        
        if (userDetails.getGender() != null) {
            existingUser.setGender(userDetails.getGender());
        }
        
        if (userDetails.getRole() != null) {
            existingUser.setRole(userDetails.getRole());
        }
        
        if (userDetails.getAccountStatus() != null) {
            existingUser.setAccountStatus(userDetails.getAccountStatus());
        }
        
        User updatedUser = userRepository.save(existingUser);
        log.info("Partially updated user with ID: {}", userId);
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
   
   @Override
   @Transactional
   public boolean updatePassword(Long userId, UserUpdatePasswordDto passwordDto) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
       
       // Check if the old password is correct
       if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
           throw new IllegalArgumentException("Old password is incorrect");
       }
       
       // Update the password
       user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
       userRepository.save(user);
       log.info("Updated password for user with ID: {}", userId);
       return true;
   }
   
   @Override
   @Transactional
   public Optional<UserGetDto> toggleUserStatus(Long userId) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
       
       // Toggle the account status
       if (user.getAccountStatus() == com.example.wildlife_backend.util.AccountStatus.ACTIVE) {
           user.setAccountStatus(com.example.wildlife_backend.util.AccountStatus.SUSPENDED);
       } else {
           user.setAccountStatus(com.example.wildlife_backend.util.AccountStatus.ACTIVE);
       }
       User updatedUser = userRepository.save(user);
       log.info("Toggled account status for user with ID: {}", userId);
       return Optional.of(convertUserToGetDto(updatedUser));
   }
   
   @Override
   @Transactional
   public boolean requestPasswordReset(String email) {
       userRepository.findByEmail(email)
               .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
       
       // In a real application, you would send an email with a reset token
       // For now, we'll just log that the request was made
       log.info("Password reset requested for user with email: {}", email);
       return true;
   }
   
   
   @Override
   @Transactional
   public Optional<UserGetDto> uploadProfilePicture(Long userId, MultipartFile file) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
       
       // For now, we'll just save the original filename as the profile picture
       // In a real application, you would save the file to a storage service and save the URL
       user.setProfilePicture(file.getOriginalFilename());
       User updatedUser = userRepository.save(user);
       log.info("Uploaded profile picture for user with ID: {}", userId);
       return Optional.of(convertUserToGetDto(updatedUser));
   }
   
   @Override
   @Transactional
   public boolean resetPassword(String token, UserUpdatePasswordDto passwordDto) {
       // In a real application, you would validate the token and find the user associated with it
       // For now, we'll just log that the reset was requested
       log.info("Password reset requested with token: {}", token);
       return true;
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