package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.UserPaymentMethod.UserPaymentMethodCreateDto;
import com.example.wildlife_backend.dto.UserPaymentMethod.UserPaymentMethodGetDto;

import java.util.List;
import java.util.Optional;

public interface UserPaymentMethodService {
    UserPaymentMethodGetDto createUserPaymentMethod(UserPaymentMethodCreateDto userPaymentMethodCreateDto);
    Optional<UserPaymentMethodGetDto> getUserPaymentMethodById(Long userPaymentMethodId);
    List<UserPaymentMethodGetDto> getAllUserPaymentMethods();
    Optional<UserPaymentMethodGetDto> updateUserPaymentMethod(Long userPaymentMethodId, UserPaymentMethodCreateDto userPaymentMethodCreateDto);
    void deleteUserPaymentMethod(Long userPaymentMethodId);

    Optional<UserPaymentMethodGetDto> setDefaultPaymentMethod(Long userPaymentMethodId);

    boolean validatePaymentMethod(Long userPaymentMethodId);

    List<ShopOrderDto> getOrderHistoryByPaymentMethod(Long userPaymentMethodId);
}