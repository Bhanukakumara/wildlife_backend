package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.payment.PaymentCreateDto;
import com.example.wildlife_backend.dto.payment.PaymentResponseDto;
import com.example.wildlife_backend.util.PaymentStatus;
import com.example.wildlife_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentCreateDto paymentDto) {
        PaymentResponseDto createdPayment = paymentService.createPayment(paymentDto);
        return ResponseEntity.ok(createdPayment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long id) {
        Optional<PaymentResponseDto> payment = paymentService.getPaymentById(id);
        return payment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reference/{paymentReference}")
    public ResponseEntity<PaymentResponseDto> getPaymentByReference(@PathVariable String paymentReference) {
        Optional<PaymentResponseDto> payment = paymentService.getPaymentByReference(paymentReference);
        return payment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentResponseDto> updatePaymentStatus(@PathVariable Long id, @RequestParam PaymentStatus status) {
        PaymentResponseDto updatedPayment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updatedPayment);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        List<PaymentResponseDto> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
}
