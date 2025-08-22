package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.UserPaymentMethod.UserPaymentMethodCreateDto;
import com.example.wildlife_backend.dto.UserPaymentMethod.UserPaymentMethodGetDto;
import com.example.wildlife_backend.service.UserPaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-payment-methods")
@CrossOrigin
@RequiredArgsConstructor
public class UserPaymentMethodController {
    private final UserPaymentMethodService userPaymentMethodService;

    // Create a new user payment method
    @PostMapping
    public ResponseEntity<UserPaymentMethodGetDto> createUserPaymentMethod(@RequestBody UserPaymentMethodCreateDto userPaymentMethodCreateDto) {
        return new ResponseEntity<>(userPaymentMethodService.createUserPaymentMethod(userPaymentMethodCreateDto), HttpStatus.CREATED);
    }

    // Get user payment method by ID
    @GetMapping("/{userPaymentMethodId}")
    public ResponseEntity<UserPaymentMethodGetDto> getUserPaymentMethodById(@PathVariable Long userPaymentMethodId) {
        Optional<UserPaymentMethodGetDto> userPaymentMethod = userPaymentMethodService.getUserPaymentMethodById(userPaymentMethodId);
        return userPaymentMethod.map(method -> new ResponseEntity<>(method, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all user payment methods
    @GetMapping
    public ResponseEntity<List<UserPaymentMethodGetDto>> getAllUserPaymentMethods() {
        List<UserPaymentMethodGetDto> userPaymentMethods = userPaymentMethodService.getAllUserPaymentMethods();
        if (userPaymentMethods.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userPaymentMethods, HttpStatus.OK);
    }

    // Update user payment method
    @PutMapping("/{userPaymentMethodId}")
    public ResponseEntity<UserPaymentMethodGetDto> updateUserPaymentMethod(@PathVariable Long userPaymentMethodId, @RequestBody UserPaymentMethodCreateDto userPaymentMethodCreateDto) {
        Optional<UserPaymentMethodGetDto> updatedUserPaymentMethod = userPaymentMethodService.updateUserPaymentMethod(userPaymentMethodId, userPaymentMethodCreateDto);
        return updatedUserPaymentMethod.map(method -> new ResponseEntity<>(method, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete user payment method
    @DeleteMapping("/{userPaymentMethodId}")
    public ResponseEntity<Void> deleteUserPaymentMethod(@PathVariable Long userPaymentMethodId) {
        try {
            userPaymentMethodService.deleteUserPaymentMethod(userPaymentMethodId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}