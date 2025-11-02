package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.user.UserRegistrationDto;
import com.example.wildlife_backend.dto.user.UserProfileDto;
import com.example.wildlife_backend.dto.user.UserResponseDto;
import com.example.wildlife_backend.util.UserRole;
import com.example.wildlife_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Public endpoints
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        UserResponseDto createdUser = userService.registerUser(registrationDto);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        return ResponseEntity.ok(available);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameAvailability(@RequestParam String username) {
        boolean available = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(available);
    }

    // Admin only endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUserResponses();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active")
    public ResponseEntity<List<UserResponseDto>> getActiveUsers() {
        List<UserResponseDto> users = userService.getActiveUserResponses();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponseDto>> getUsersByRole(@PathVariable UserRole role) {
        List<UserResponseDto> users = userService.getUserResponsesByRole(role);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/suspend")
    public ResponseEntity<Void> suspendUser(@PathVariable Long id, @RequestParam String reason) {
        userService.suspendUser(id, reason);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    // User profile endpoints (authenticated users)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        Optional<UserResponseDto> user = userService.getUserResponseById(id);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserProfileDto profileDto) {
        UserResponseDto updatedUser = userService.updateProfile(id, profileDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserResponseDto> updateProfile(@PathVariable Long id, @RequestBody UserProfileDto profileDto) {
        UserResponseDto updatedUser = userService.updateProfile(id, profileDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,
                                             @RequestParam String oldPassword,
                                             @RequestParam String newPassword) {
        userService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // Statistics endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/count")
    public ResponseEntity<Long> getTotalUserCount() {
        long count = userService.getTotalUserCount();
        return ResponseEntity.ok(count);
    }
}
