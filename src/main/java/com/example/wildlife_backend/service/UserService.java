package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.UserCreateDto;
import com.example.wildlife_backend.dto.UserGetDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Core CRUD Operations
    UserGetDto createUser(UserCreateDto userCreateDto);
    Optional<UserGetDto> getUserById(Long userId);
    Optional<UserGetDto> getUserByEmail(String email);
    List<UserGetDto> getAllUsers();
    Optional<UserGetDto> updateUser(Long userId, UserCreateDto userDetails);
    boolean deleteUser(Long userId);

}
