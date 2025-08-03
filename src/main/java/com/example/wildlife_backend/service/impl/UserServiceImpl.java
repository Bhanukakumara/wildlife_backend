package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.UserCreateDto;
import com.example.wildlife_backend.dto.UserGetDto;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.exception.DuplicateResourceException;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.UserService;
import com.example.wildlife_backend.util.UserStatus;
import com.example.wildlife_backend.util.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserGetDto createUser(UserCreateDto userCreateDto) {
        validateUserCreation(userCreateDto);

        User user = convertCreateDtoToUser(userCreateDto);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        log.info("Created new user with ID: {}", savedUser.getUserId());
        return convertUserToGetDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserGetDto> getUserById(Long userId) {
        return Optional.ofNullable(userRepository.findById(userId)
                .map(this::convertUserToGetDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId)));
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
        if (!all.isEmpty()) {
            return all.stream().map(this::convertUserToGetDto).collect(Collectors.toList());
        }
        throw new ResourceNotFoundException("Users not found");
    }

    @Override
    @Transactional
    public boolean updateUser(Long userId, UserCreateDto userDetails) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (!(existingUser == null)){
            validateUserUpdate(existingUser, userDetails);
            updateUserFromDto(existingUser, userDetails);
            userRepository.save(existingUser);
            log.info("Updated user with ID: {}", userId);
            return true;
        }
        throw new ResourceNotFoundException("User not found with id: " + userId);
    }

    @Override
    @Transactional
    public boolean deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (!(user == null)){
            userRepository.delete(user);
            log.info("Deleted user with ID: {}", userId);
            return true;
        }
        throw new ResourceNotFoundException("User not found with id: " + userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGetDto> findUsersByStatus(UserStatus status) {
        List<User> byUserStatus = userRepository.findByUserStatus(status);
        if (!byUserStatus.isEmpty()) {
            return byUserStatus.stream().map(this::convertUserToGetDto).collect(Collectors.toList());
        }
        throw new ResourceNotFoundException("Users not found");
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGetDto> findUsersByType(UserType userType) {
        List<User> byUserType = userRepository.findByUserType(userType);
        if (!byUserType.isEmpty()) {
            return byUserType.stream().map(this::convertUserToGetDto).collect(Collectors.toList());
        }
        throw new ResourceNotFoundException("Users not found");
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGetDto> searchUsersByName(String name, String lastName) {
        List<User> byFirstNameContainingOrLastNameContainingIgnoreCase = userRepository.findByFirstNameContainingOrLastNameContainingIgnoreCase(name,lastName);
        if (!byFirstNameContainingOrLastNameContainingIgnoreCase.isEmpty()) {
            return byFirstNameContainingOrLastNameContainingIgnoreCase.stream().map(this::convertUserToGetDto).collect(Collectors.toList());
        }
        throw new ResourceNotFoundException("Users not found");
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGetDto> findUsersByDateOfBirthRange(LocalDate startDate, LocalDate endDate) {
        List<User> byDateOfBirthBetween = userRepository.findByDateOfBirthBetween(startDate, endDate);
        if (!byDateOfBirthBetween.isEmpty()) {
            return byDateOfBirthBetween.stream().map(this::convertUserToGetDto).collect(Collectors.toList());
        }
        throw new ResourceNotFoundException("Users not found");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPhoneUnique(String phone) {
        return !userRepository.existsByPhone(phone);
    }

    private void validateUserCreation(UserCreateDto userCreateDto) {
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + userCreateDto.getEmail());
        }

        if (userRepository.existsByPhone(userCreateDto.getPhone())) {
            throw new DuplicateResourceException("Phone number already in use: " + userCreateDto.getPhone());
        }
    }

    private void validateUserUpdate(User existingUser, UserCreateDto userDetails) {
        if (!existingUser.getEmail().equals(userDetails.getEmail()) &&
                userRepository.existsByEmail(userDetails.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + userDetails.getEmail());
        }

        if (!existingUser.getPhone().equals(userDetails.getPhone()) &&
                userRepository.existsByPhone(userDetails.getPhone())) {
            throw new DuplicateResourceException("Phone number already in use: " + userDetails.getPhone());
        }
    }

    private User convertCreateDtoToUser(UserCreateDto userCreateDto) {
        return User.builder()
                .email(userCreateDto.getEmail())
                .password(userCreateDto.getPassword())
                .firstName(userCreateDto.getFirstName())
                .lastName(userCreateDto.getLastName())
                .phone(userCreateDto.getPhone())
                .dateOfBirth(userCreateDto.getDateOfBirth())
                .userType(userCreateDto.getUserType())
                .userStatus(userCreateDto.getUserStatus())
                .build();
    }

    private UserGetDto convertUserToGetDto(User user) {
        return UserGetDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .dateOfBirth(user.getDateOfBirth())
                .userType(user.getUserType())
                .userStatus(user.getUserStatus())
                .build();
    }

    private void updateUserFromDto(User user, UserCreateDto dto) {
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
//            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setPassword(dto.getPassword());
        }
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setUserType(dto.getUserType());
        user.setUserStatus(dto.getUserStatus());
    }
}