package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.UserPaymentMethod.UserPaymentMethodCreateDto;
import com.example.wildlife_backend.dto.UserPaymentMethod.UserPaymentMethodGetDto;
import com.example.wildlife_backend.service.UserPaymentMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-payment-methods")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class UserPaymentMethodController {
    private final UserPaymentMethodService userPaymentMethodService;

    // Create a new user payment method with validation
    @PostMapping
    public ResponseEntity<UserPaymentMethodGetDto> createUserPaymentMethod(
            @Valid @RequestBody UserPaymentMethodCreateDto userPaymentMethodCreateDto) {
        try {
            log.info("Creating new payment method for user: {}", userPaymentMethodCreateDto.getUserId());
            UserPaymentMethodGetDto createdMethod = userPaymentMethodService.createUserPaymentMethod(userPaymentMethodCreateDto);
            return new ResponseEntity<>(createdMethod, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Failed to create payment method: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get user payment method by ID
    @GetMapping("/{userPaymentMethodId}")
    public ResponseEntity<UserPaymentMethodGetDto> getUserPaymentMethodById(@PathVariable Long userPaymentMethodId) {
        log.info("Retrieving payment method with ID: {}", userPaymentMethodId);
        Optional<UserPaymentMethodGetDto> userPaymentMethod = userPaymentMethodService.getUserPaymentMethodById(userPaymentMethodId);
        return userPaymentMethod.map(method -> new ResponseEntity<>(method, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all user payment methods with pagination, sorting, and filtering by user ID
//    @GetMapping
//    public ResponseEntity<Page<UserPaymentMethodGetDto>> getAllUserPaymentMethods(
//            @RequestParam(required = false) Long userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id,asc") String[] sort) {
//        try {
//            Sort.Direction direction = Sort.Direction.fromString(sort[1]);
//            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
//            Page<UserPaymentMethodGetDto> paymentMethods = userPaymentMethodService.getAllUserPaymentMethods();
//            return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Error retrieving payment methods: {}", e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    // Update user payment method with partial update support
//    @PatchMapping("/{userPaymentMethodId}")
//    public ResponseEntity<UserPaymentMethodGetDto> updateUserPaymentMethod(
//            @PathVariable Long userPaymentMethodId,
//            @Valid @RequestBody UserPaymentMethodUpdateDto userPaymentMethodUpdateDto) {
//        log.info("Updating payment method with ID: {}", userPaymentMethodId);
//        Optional<UserPaymentMethodGetDto> updatedUserPaymentMethod = userPaymentMethodService.updateUserPaymentMethod(userPaymentMethodId, userPaymentMethodUpdateDto);
//        return updatedUserPaymentMethod.map(method -> new ResponseEntity<>(method, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

    // Set a payment method as default
    @PatchMapping("/{userPaymentMethodId}/set-default")
    public ResponseEntity<UserPaymentMethodGetDto> setDefaultPaymentMethod(@PathVariable Long userPaymentMethodId) {
        try {
            log.info("Setting payment method with ID {} as default", userPaymentMethodId);
            Optional<UserPaymentMethodGetDto> updatedMethod = userPaymentMethodService.setDefaultPaymentMethod(userPaymentMethodId);
            return updatedMethod.map(method -> new ResponseEntity<>(method, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalStateException e) {
            log.error("Failed to set default payment method: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Validate payment method (e.g., check expiry date or provider status)
    @PostMapping("/{userPaymentMethodId}/validate")
    public ResponseEntity<Boolean> validatePaymentMethod(@PathVariable Long userPaymentMethodId) {
        log.info("Validating payment method with ID: {}", userPaymentMethodId);
        boolean isValid = userPaymentMethodService.validatePaymentMethod(userPaymentMethodId);
        return new ResponseEntity<>(isValid, isValid ? HttpStatus.OK : HttpStatus.PAYMENT_REQUIRED);
    }

    // Get payment methods linked to orders
//    @GetMapping("/{userPaymentMethodId}/order-history")
//    public ResponseEntity<List<ShopOrderDto>> getOrderHistoryByPaymentMethod(@PathVariable Long userPaymentMethodId) {
//        try {
//            log.info("Retrieving order history for payment method ID: {}", userPaymentMethodId);
//            List<ShopOrderDto> orderHistory = userPaymentMethodService.getOrderHistoryByPaymentMethod(userPaymentMethodId);
//            return new ResponseEntity<>(orderHistory, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Error retrieving order history: {}", e.getMessage());
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    // Delete user payment method with soft delete option
//    @DeleteMapping("/{userPaymentMethodId}")
//    public ResponseEntity<Void> deleteUserPaymentMethod(@PathVariable Long userPaymentMethodId,
//                                                        @RequestParam(defaultValue = "false") boolean softDelete) {
//        try {
//            log.info("Deleting payment method with ID: {}, softDelete: {}", userPaymentMethodId, softDelete);
//            userPaymentMethodService.deleteUserPaymentMethod(userPaymentMethodId, softDelete);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            log.error("Failed to delete payment method: {}", e.getMessage());
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}