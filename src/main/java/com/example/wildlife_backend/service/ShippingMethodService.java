package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.ShippingMethod.ShippingMethodCreateDto;
import com.example.wildlife_backend.dto.ShippingMethod.ShippingMethodGetDto;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ShippingMethodService {
    ShippingMethodGetDto createShippingMethod(ShippingMethodCreateDto shippingMethodCreateDto);
    Optional<ShippingMethodGetDto> getShippingMethodById(Long shippingMethodId);
    List<ShippingMethodGetDto> getAllShippingMethods();
    Optional<ShippingMethodGetDto> updateShippingMethod(Long shippingMethodId, ShippingMethodCreateDto shippingMethodCreateDto);
    boolean deleteShippingMethod(Long shippingMethodId);

    List<ShippingMethodGetDto> searchShippingMethodsByName(String name);

    List<ShippingMethodGetDto> getShippingMethodsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    List<ShippingMethodGetDto> bulkCreateShippingMethods(@Valid List<ShippingMethodCreateDto> shippingMethodCreateDtos);

    boolean validateShippingMethod(@Valid ShippingMethodCreateDto shippingMethodCreateDto);

    List<ShippingMethodGetDto> getShippingMethodsWithShopOrders();
}