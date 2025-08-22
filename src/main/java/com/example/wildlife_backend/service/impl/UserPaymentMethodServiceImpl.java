package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.UserPaymentMethod.UserPaymentMethodCreateDto;
import com.example.wildlife_backend.dto.UserPaymentMethod.UserPaymentMethodGetDto;
import com.example.wildlife_backend.entity.PaymentType;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.entity.UserPaymentMethod;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.PaymentTypeRepository;
import com.example.wildlife_backend.repository.UserPaymentMethodRepository;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.UserPaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPaymentMethodServiceImpl implements UserPaymentMethodService {
    private final UserPaymentMethodRepository userPaymentMethodRepository;
    private final UserRepository userRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    @Override
    public UserPaymentMethodGetDto createUserPaymentMethod(UserPaymentMethodCreateDto userPaymentMethodCreateDto) {
        UserPaymentMethod userPaymentMethod = new UserPaymentMethod();
        
        // Set user
        if (userPaymentMethodCreateDto.getUserId() != null) {
            User user = userRepository.findById(userPaymentMethodCreateDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userPaymentMethodCreateDto.getUserId()));
            userPaymentMethod.setUser(user);
        }
        
        // Set payment type
        if (userPaymentMethodCreateDto.getPaymentTypeId() != null) {
            PaymentType paymentType = paymentTypeRepository.findById(userPaymentMethodCreateDto.getPaymentTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("PaymentType not found with id: " + userPaymentMethodCreateDto.getPaymentTypeId()));
            userPaymentMethod.setPaymentType(paymentType);
        }
        
        userPaymentMethod.setProvider(userPaymentMethodCreateDto.getProvider());
        userPaymentMethod.setAccountNumber(userPaymentMethodCreateDto.getAccountNumber());
        userPaymentMethod.setExpiryDate(userPaymentMethodCreateDto.getExpiryDate());
        userPaymentMethod.setDefault(userPaymentMethodCreateDto.isDefault());
        
        UserPaymentMethod savedUserPaymentMethod = userPaymentMethodRepository.save(userPaymentMethod);
        return convertToGetDto(savedUserPaymentMethod);
    }

    @Override
    public Optional<UserPaymentMethodGetDto> getUserPaymentMethodById(Long userPaymentMethodId) {
        return userPaymentMethodRepository.findById(userPaymentMethodId)
                .map(this::convertToGetDto);
    }

    @Override
    public List<UserPaymentMethodGetDto> getAllUserPaymentMethods() {
        return userPaymentMethodRepository.findAll()
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserPaymentMethodGetDto> updateUserPaymentMethod(Long userPaymentMethodId, UserPaymentMethodCreateDto userPaymentMethodCreateDto) {
        UserPaymentMethod existingUserPaymentMethod = userPaymentMethodRepository.findById(userPaymentMethodId)
                .orElseThrow(() -> new ResourceNotFoundException("UserPaymentMethod not found with id: " + userPaymentMethodId));
        
        // Update user if provided
        if (userPaymentMethodCreateDto.getUserId() != null) {
            User user = userRepository.findById(userPaymentMethodCreateDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userPaymentMethodCreateDto.getUserId()));
            existingUserPaymentMethod.setUser(user);
        }
        
        // Update payment type if provided
        if (userPaymentMethodCreateDto.getPaymentTypeId() != null) {
            PaymentType paymentType = paymentTypeRepository.findById(userPaymentMethodCreateDto.getPaymentTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("PaymentType not found with id: " + userPaymentMethodCreateDto.getPaymentTypeId()));
            existingUserPaymentMethod.setPaymentType(paymentType);
        }
        
        existingUserPaymentMethod.setProvider(userPaymentMethodCreateDto.getProvider());
        existingUserPaymentMethod.setAccountNumber(userPaymentMethodCreateDto.getAccountNumber());
        existingUserPaymentMethod.setExpiryDate(userPaymentMethodCreateDto.getExpiryDate());
        existingUserPaymentMethod.setDefault(userPaymentMethodCreateDto.isDefault());
        
        UserPaymentMethod updatedUserPaymentMethod = userPaymentMethodRepository.save(existingUserPaymentMethod);
        return Optional.of(convertToGetDto(updatedUserPaymentMethod));
    }

    @Override
    public void deleteUserPaymentMethod(Long userPaymentMethodId) {
        if (!userPaymentMethodRepository.existsById(userPaymentMethodId)) {
            throw new ResourceNotFoundException("UserPaymentMethod not found with id: " + userPaymentMethodId);
        }
        userPaymentMethodRepository.deleteById(userPaymentMethodId);
    }

    private UserPaymentMethodGetDto convertToGetDto(UserPaymentMethod userPaymentMethod) {
        return UserPaymentMethodGetDto.builder()
                .id(userPaymentMethod.getId())
                .userId(userPaymentMethod.getUser() != null ? userPaymentMethod.getUser().getId() : null)
                .paymentTypeId(userPaymentMethod.getPaymentType() != null ? userPaymentMethod.getPaymentType().getId() : null)
                .provider(userPaymentMethod.getProvider())
                .accountNumber(userPaymentMethod.getAccountNumber())
                .expiryDate(userPaymentMethod.getExpiryDate())
                .isDefault(userPaymentMethod.isDefault())
                .build();
    }
}