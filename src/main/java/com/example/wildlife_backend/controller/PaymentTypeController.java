package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.PaymentType.PaymentTypeCreateDto;
import com.example.wildlife_backend.dto.PaymentType.PaymentTypeGetDto;
import com.example.wildlife_backend.service.PaymentTypeService;
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
    public ResponseEntity<PaymentTypeGetDto> createPaymentType(@RequestBody PaymentTypeCreateDto paymentTypeCreateDto) {
        return new ResponseEntity<>(paymentTypeService.createPaymentType(paymentTypeCreateDto), HttpStatus.CREATED);
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

    // Update payment type
    @PutMapping("/{paymentTypeId}")
    public ResponseEntity<PaymentTypeGetDto> updatePaymentType(@PathVariable Long paymentTypeId, @RequestBody PaymentTypeCreateDto paymentTypeCreateDto) {
        Optional<PaymentTypeGetDto> updatedPaymentType = paymentTypeService.updatePaymentType(paymentTypeId, paymentTypeCreateDto);
        return updatedPaymentType.map(type -> new ResponseEntity<>(type, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete payment type
    @DeleteMapping("/{paymentTypeId}")
    public ResponseEntity<Void> deletePaymentType(@PathVariable Long paymentTypeId) {
        try {
            paymentTypeService.deletePaymentType(paymentTypeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}