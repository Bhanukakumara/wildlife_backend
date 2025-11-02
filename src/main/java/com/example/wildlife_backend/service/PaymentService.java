package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.payment.PaymentCreateDto;
import com.example.wildlife_backend.dto.payment.PaymentResponseDto;
import com.example.wildlife_backend.util.PaymentStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentCreateDto paymentDto);
    PaymentResponseDto updatePaymentStatus(Long id, PaymentStatus status);
    Optional<PaymentResponseDto> getPaymentById(Long id);
    Optional<PaymentResponseDto> getPaymentByOrderId(Long orderId);
    Optional<PaymentResponseDto> getPaymentByReference(String paymentReference);
    List<PaymentResponseDto> getPaymentsByStatus(PaymentStatus status);
    List<PaymentResponseDto> getAllPayments();
}
