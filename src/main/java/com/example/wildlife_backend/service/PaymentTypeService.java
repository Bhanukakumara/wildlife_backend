package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.PaymentType.PaymentTypeCreateDto;
import com.example.wildlife_backend.dto.PaymentType.PaymentTypeGetDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface PaymentTypeService {
    PaymentTypeGetDto createPaymentType(PaymentTypeCreateDto paymentTypeCreateDto);
    Optional<PaymentTypeGetDto> getPaymentTypeById(Long paymentTypeId);
    List<PaymentTypeGetDto> getAllPaymentTypes();
    Optional<PaymentTypeGetDto> updatePaymentType(Long paymentTypeId, PaymentTypeCreateDto paymentTypeCreateDto);
    boolean deletePaymentType(Long paymentTypeId);

    Optional<PaymentTypeGetDto> getPaymentTypeByValue(String value);

    List<PaymentTypeGetDto> searchPaymentTypesByValue(String value);

    List<PaymentTypeGetDto> bulkCreatePaymentTypes(@Valid List<PaymentTypeCreateDto> paymentTypeCreateDtos);

    boolean validatePaymentType(@Valid PaymentTypeCreateDto paymentTypeCreateDto);

    List<PaymentTypeGetDto> getPaymentTypesWithUserPaymentMethods();
}