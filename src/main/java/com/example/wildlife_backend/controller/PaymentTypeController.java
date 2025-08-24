package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.PaymentType.PaymentTypeCreateDto;
import com.example.wildlife_backend.dto.PaymentType.PaymentTypeGetDto;
import com.example.wildlife_backend.service.PaymentTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment-types")
@CrossOrigin
@RequiredArgsConstructor
public class PaymentTypeController {
    private final PaymentTypeService paymentTypeService;

    // Create a new payment type
    @PostMapping
    public ResponseEntity<PaymentTypeGetDto> createPaymentType(@Valid @RequestBody PaymentTypeCreateDto paymentTypeCreateDto) {
        PaymentTypeGetDto createdPaymentType = paymentTypeService.createPaymentType(paymentTypeCreateDto);
        return new ResponseEntity<>(createdPaymentType, HttpStatus.CREATED);
    }

    // Get payment type by ID
    @GetMapping("/{paymentTypeId}")
    public ResponseEntity<PaymentTypeGetDto> getPaymentTypeById(@PathVariable Long paymentTypeId) {
        Optional<PaymentTypeGetDto> paymentType = paymentTypeService.getPaymentTypeById(paymentTypeId);
        return paymentType.map(type -> new ResponseEntity<>(type, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all payment types
    @GetMapping
    public ResponseEntity<List<PaymentTypeGetDto>> getAllPaymentTypes() {
        List<PaymentTypeGetDto> paymentTypes = paymentTypeService.getAllPaymentTypes();
        if (paymentTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(paymentTypes, HttpStatus.OK);
    }

    // Get payment type by value
    @GetMapping("/by-value/{value}")
    public ResponseEntity<PaymentTypeGetDto> getPaymentTypeByValue(@PathVariable String value) {
        Optional<PaymentTypeGetDto> paymentType = paymentTypeService.getPaymentTypeByValue(value);
        return paymentType.map(type -> new ResponseEntity<>(type, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Search payment types by value (partial match)
    @GetMapping("/search")
    public ResponseEntity<List<PaymentTypeGetDto>> searchPaymentTypesByValue(@RequestParam String value) {
        List<PaymentTypeGetDto> paymentTypes = paymentTypeService.searchPaymentTypesByValue(value);
        if (paymentTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(paymentTypes, HttpStatus.OK);
    }

    // Update payment type
    @PutMapping("/{paymentTypeId}")
    public ResponseEntity<PaymentTypeGetDto> updatePaymentType(
            @PathVariable Long paymentTypeId,
            @Valid @RequestBody PaymentTypeCreateDto paymentTypeCreateDto) {
        Optional<PaymentTypeGetDto> updatedPaymentType = paymentTypeService.updatePaymentType(paymentTypeId, paymentTypeCreateDto);
        return updatedPaymentType.map(type -> new ResponseEntity<>(type, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete payment type
    @DeleteMapping("/{paymentTypeId}")
    public ResponseEntity<Boolean> deletePaymentType(@PathVariable Long paymentTypeId) {
        boolean deleted = paymentTypeService.deletePaymentType(paymentTypeId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Bulk create payment types
    @PostMapping("/bulk-create")
    public ResponseEntity<List<PaymentTypeGetDto>> bulkCreatePaymentTypes(
            @Valid @RequestBody List<PaymentTypeCreateDto> paymentTypeCreateDtos) {
        List<PaymentTypeGetDto> createdPaymentTypes = paymentTypeService.bulkCreatePaymentTypes(paymentTypeCreateDtos);
        return new ResponseEntity<>(createdPaymentTypes, HttpStatus.CREATED);
    }

    // Validate payment type data
    @PostMapping("/validate")
    public ResponseEntity<String> validatePaymentType(@Valid @RequestBody PaymentTypeCreateDto paymentTypeCreateDto) {
        boolean isValid = paymentTypeService.validatePaymentType(paymentTypeCreateDto);
        if (isValid) {
            return new ResponseEntity<>("Payment type data is valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid payment type data", HttpStatus.BAD_REQUEST);
    }

    // Get payment types with associated user payment methods
    @GetMapping("/with-user-payment-methods")
    public ResponseEntity<List<PaymentTypeGetDto>> getPaymentTypesWithUserPaymentMethods() {
        List<PaymentTypeGetDto> paymentTypes = paymentTypeService.getPaymentTypesWithUserPaymentMethods();
        if (paymentTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(paymentTypes, HttpStatus.OK);
    }
}