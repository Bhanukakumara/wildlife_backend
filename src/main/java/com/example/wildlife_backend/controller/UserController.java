package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.UserCreateDto;
import com.example.wildlife_backend.dto.UserGetDto;
import com.example.wildlife_backend.service.UserService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create a new user
    @PostMapping("create")
    public ResponseEntity<UserGetDto> createUser(@RequestBody UserCreateDto userCreateDto) {
        return new ResponseEntity<>(userService.createUser(userCreateDto), HttpStatus.CREATED);
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
        return userByEmail.map(userGetDto -> new ResponseEntity<>(userGetDto, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all users
    @GetMapping("/get-all")
    public ResponseEntity<List<UserGetDto>> getAllUsers() {
        List<UserGetDto> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    // Update user details
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserGetDto> updateUser(@PathVariable Long userId, @RequestBody UserCreateDto userCreateDto) {
        Optional<UserGetDto> updatedUser = userService.updateUser(userId, userCreateDto);
        return updatedUser.map(userGetDto -> new ResponseEntity<>(userGetDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete user
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
