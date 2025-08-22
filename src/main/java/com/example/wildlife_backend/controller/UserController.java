package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.user.UserCreateDto;
import com.example.wildlife_backend.dto.user.UserGetDto;
import com.example.wildlife_backend.dto.user.UserSearchDto;
import com.example.wildlife_backend.dto.user.UserUpdatePasswordDto;
import com.example.wildlife_backend.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create a new user with validation
    @PostMapping("/create")
    public ResponseEntity<UserGetDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        try {
            UserGetDto createdUser = userService.createUser(userCreateDto);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Get user by ID
    @GetMapping("/get-by-userId/{userId}")
    public ResponseEntity<UserGetDto> getUserById(@PathVariable Long userId) {
        Optional<UserGetDto> userById = userService.getUserById(userId);
        return userById.map(userGetDto -> new ResponseEntity<>(userGetDto, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get user by email
    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<UserGetDto> getUserByEmail(@PathVariable @Email String email) {
        Optional<UserGetDto> userByEmail = userService.getUserByEmail(email);
        return userByEmail.map(userGetDto -> new ResponseEntity<>(userGetDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all users with pagination and sorting
    @GetMapping("/get-all")
    public ResponseEntity<Page<UserGetDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sort[1]);
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
            Page<UserGetDto> users = userService.getAllUsers(pageable);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Search users with filters
    @PostMapping("/search")
    public ResponseEntity<Page<UserGetDto>> searchUsers(
            @Valid @RequestBody UserSearchDto searchDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sort[1]);
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
            Page<UserGetDto> users = userService.searchUsers(searchDto, pageable);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Update user details (full update)
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserGetDto> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserCreateDto userCreateDto) {
        Optional<UserGetDto> updatedUser = userService.updateUser(userId, userCreateDto);
        return updatedUser.map(userGetDto -> new ResponseEntity<>(userGetDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Partial update for specific fields
    @PatchMapping("/partial-update/{userId}")
    public ResponseEntity<UserGetDto> partialUpdateUser(
            @PathVariable Long userId,
            @RequestBody UserCreateDto userCreateDto) {
        Optional<UserGetDto> updatedUser = userService.partialUpdateUser(userId, userCreateDto);
        return updatedUser.map(userGetDto -> new ResponseEntity<>(userGetDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update password
    @PatchMapping("/update-password/{userId}")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdatePasswordDto passwordDto) {
        boolean updated = userService.updatePassword(userId, passwordDto);
        return updated ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Upload profile picture
    @PostMapping(value = "/upload-profile-picture/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserGetDto> uploadProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            Optional<UserGetDto> updatedUser = userService.uploadProfilePicture(userId, file);
            return updatedUser.map(userGetDto -> new ResponseEntity<>(userGetDto, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Delete user (soft delete)
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Activate/deactivate user account
    @PatchMapping("/toggle-status/{userId}")
    public ResponseEntity<UserGetDto> toggleUserStatus(@PathVariable Long userId) {
        Optional<UserGetDto> updatedUser = userService.toggleUserStatus(userId);
        return updatedUser.map(userGetDto -> new ResponseEntity<>(userGetDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Request password reset
    @PostMapping("/request-password-reset")
    public ResponseEntity<Void> requestPasswordReset(@RequestParam @Email String email) {
        boolean requested = userService.requestPasswordReset(email);
        return requested ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Reset password with token
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestParam String token,
            @Valid @RequestBody UserUpdatePasswordDto passwordDto) {
        boolean reset = userService.resetPassword(token, passwordDto);
        return reset ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
