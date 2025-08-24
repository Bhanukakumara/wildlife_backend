package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.user.UserCreateDto;
import com.example.wildlife_backend.dto.user.UserGetDto;
import com.example.wildlife_backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserGetDto> registerUser(
            @Valid @RequestBody UserCreateDto userCreateDto,
            HttpServletRequest request) {
        try {
            log.info("Attempting to register new user with email: {}", userCreateDto.getEmail());

            UserGetDto createdUser = userService.createUser(userCreateDto);

            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            log.warn("Failed to register user - validation error: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error during user registration", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserGetDto> getUserById(
            @PathVariable Long userId) {

        Optional<UserGetDto> user = userService.getUserById(userId);
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserGetDto> getUserByEmail( @PathVariable @Email String email) {

        Optional<UserGetDto> user = userService.getUserByEmail(email);

        return user.map(userGetDto -> new ResponseEntity<>(userGetDto, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<UserGetDto>> getAllUsers(){
        List<UserGetDto> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserGetDto> updateUserProfile(
            @Valid @RequestBody UserCreateDto userCreateDto) {
        UserGetDto userGetDto = userService.createUser(userCreateDto);
        if (userGetDto.getEmail() != null){
            return new ResponseEntity<>(userGetDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(userGetDto, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserGetDto> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserCreateDto userCreateDto) {

        Optional<UserGetDto> updatedUser = userService.updateUser(userId, userCreateDto);

        return updatedUser.map(userDto -> {
                    log.info("User updated by admin - ID: {}", userId);
                    return new ResponseEntity<>(userDto, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);

        if (deleted) {
            log.warn("User permanently deleted by admin - ID: {}", userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
