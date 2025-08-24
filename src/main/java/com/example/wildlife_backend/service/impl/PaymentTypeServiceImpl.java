package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.PaymentType.PaymentTypeCreateDto;
import com.example.wildlife_backend.dto.PaymentType.PaymentTypeGetDto;
import com.example.wildlife_backend.entity.PaymentType;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.PaymentTypeRepository;
import com.example.wildlife_backend.service.PaymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentTypeServiceImpl implements PaymentTypeService {
    private final PaymentTypeRepository paymentTypeRepository;

    @Override
public PaymentTypeGetDto createPaymentType(PaymentTypeCreateDto paymentTypeCreateDto) {
        PaymentType paymentType = new PaymentType();
        paymentType.setValue(paymentTypeCreateDto.getValue());
        
        PaymentType savedPaymentType = paymentTypeRepository.save(paymentType);
        return convertToGetDto(savedPaymentType);
    }

    @Override
    public Optional<PaymentTypeGetDto> getPaymentTypeById(Long paymentTypeId) {
        return paymentTypeRepository.findById(paymentTypeId)
                .map(this::convertToGetDto);
    }

    @Override
    public List<PaymentTypeGetDto> getAllPaymentTypes() {
        return paymentTypeRepository.findAll()
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentTypeGetDto> updatePaymentType(Long paymentTypeId, PaymentTypeCreateDto paymentTypeCreateDto) {
        PaymentType existingPaymentType = paymentTypeRepository.findById(paymentTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentType not found with id: " + paymentTypeId));
        
        existingPaymentType.setValue(paymentTypeCreateDto.getValue());
        
        PaymentType updatedPaymentType = paymentTypeRepository.save(existingPaymentType);
        return Optional.of(convertToGetDto(updatedPaymentType));
    }

    @Override
    public boolean deletePaymentType(Long paymentTypeId) {
        if (!paymentTypeRepository.existsById(paymentTypeId)) {
            throw new ResourceNotFoundException("PaymentType not found with id: " + paymentTypeId);
        }
        else {
            paymentTypeRepository.deleteById(paymentTypeId);
            return true;
        }
    }

    @Override
    public Optional<PaymentTypeGetDto> getPaymentTypeByValue(String value) {
        return Optional.empty();
    }

    @Override
    public List<PaymentTypeGetDto> searchPaymentTypesByValue(String value) {
        return List.of();
    }

    @Override
    public List<PaymentTypeGetDto> bulkCreatePaymentTypes(List<PaymentTypeCreateDto> paymentTypeCreateDtos) {
        return List.of();
    }

    @Override
    public boolean validatePaymentType(PaymentTypeCreateDto paymentTypeCreateDto) {
        return false;
    }

    @Override
    public List<PaymentTypeGetDto> getPaymentTypesWithUserPaymentMethods() {
        return List.of();
    }

    private PaymentTypeGetDto convertToGetDto(PaymentType paymentType) {
        return PaymentTypeGetDto.builder()
                .id(paymentType.getId())
                .value(paymentType.getValue())
                .build();
    }
}