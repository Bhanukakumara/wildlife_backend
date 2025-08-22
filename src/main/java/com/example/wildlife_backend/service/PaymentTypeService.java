package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.PaymentType.PaymentTypeCreateDto;
import com.example.wildlife_backend.dto.PaymentType.PaymentTypeGetDto;

import java.util.List;
import java.util.Optional;

public interface PaymentTypeService {
    PaymentTypeGetDto createPaymentType(PaymentTypeCreateDto paymentTypeCreateDto);
    Optional<PaymentTypeGetDto> getPaymentTypeById(Long paymentTypeId);
    List<PaymentTypeGetDto> getAllPaymentTypes();
    Optional<PaymentTypeGetDto> updatePaymentType(Long paymentTypeId, PaymentTypeCreateDto paymentTypeCreateDto);
    void deletePaymentType(Long paymentTypeId);
}