package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.user.UserCreateDto;
import com.example.wildlife_backend.dto.user.UserGetDto;
import com.example.wildlife_backend.dto.user.UserSearchDto;
import com.example.wildlife_backend.dto.user.UserUpdatePasswordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Core CRUD Operations
    UserGetDto createUser(UserCreateDto userCreateDto);
    Optional<UserGetDto> getUserById(Long userId);
    Optional<UserGetDto> getUserByEmail(String email);
    List<UserGetDto> getAllUsers();
    Page<UserGetDto> getAllUsers(Pageable pageable);
    Optional<UserGetDto> updateUser(Long userId, UserCreateDto userDetails);
    boolean deleteUser(Long userId);
    
    // Additional operations
    Page<UserGetDto> searchUsers(UserSearchDto searchDto, Pageable pageable);
    Optional<UserGetDto> partialUpdateUser(Long userId, UserCreateDto userDetails);
    boolean updatePassword(Long userId, UserUpdatePasswordDto passwordDto);
    Optional<UserGetDto> uploadProfilePicture(Long userId, MultipartFile file);
    Optional<UserGetDto> toggleUserStatus(Long userId);
    boolean requestPasswordReset(String email);
    boolean resetPassword(String token, UserUpdatePasswordDto passwordDto);

}
