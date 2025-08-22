package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.ShippingMethod.ShippingMethodCreateDto;
import com.example.wildlife_backend.dto.ShippingMethod.ShippingMethodGetDto;

import java.util.List;
import java.util.Optional;

public interface ShippingMethodService {
    ShippingMethodGetDto createShippingMethod(ShippingMethodCreateDto shippingMethodCreateDto);
    Optional<ShippingMethodGetDto> getShippingMethodById(Long shippingMethodId);
    List<ShippingMethodGetDto> getAllShippingMethods();
    Optional<ShippingMethodGetDto> updateShippingMethod(Long shippingMethodId, ShippingMethodCreateDto shippingMethodCreateDto);
    void deleteShippingMethod(Long shippingMethodId);
}