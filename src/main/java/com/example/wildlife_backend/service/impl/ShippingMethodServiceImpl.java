package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.ShippingMethod.ShippingMethodCreateDto;
import com.example.wildlife_backend.dto.ShippingMethod.ShippingMethodGetDto;
import com.example.wildlife_backend.entity.ShippingMethod;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.ShippingMethodRepository;
import com.example.wildlife_backend.service.ShippingMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingMethodServiceImpl implements ShippingMethodService {
    private final ShippingMethodRepository shippingMethodRepository;

    @Override
    public ShippingMethodGetDto createShippingMethod(ShippingMethodCreateDto shippingMethodCreateDto) {
        ShippingMethod shippingMethod = new ShippingMethod();
        shippingMethod.setName(shippingMethodCreateDto.getName());
        shippingMethod.setPrice(shippingMethodCreateDto.getPrice());
        
        ShippingMethod savedShippingMethod = shippingMethodRepository.save(shippingMethod);
        return convertToGetDto(savedShippingMethod);
    }

    @Override
    public Optional<ShippingMethodGetDto> getShippingMethodById(Long shippingMethodId) {
        return shippingMethodRepository.findById(shippingMethodId)
                .map(this::convertToGetDto);
    }

    @Override
    public List<ShippingMethodGetDto> getAllShippingMethods() {
        return shippingMethodRepository.findAll()
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ShippingMethodGetDto> updateShippingMethod(Long shippingMethodId, ShippingMethodCreateDto shippingMethodCreateDto) {
        ShippingMethod existingShippingMethod = shippingMethodRepository.findById(shippingMethodId)
                .orElseThrow(() -> new ResourceNotFoundException("ShippingMethod not found with id: " + shippingMethodId));
        
        existingShippingMethod.setName(shippingMethodCreateDto.getName());
        existingShippingMethod.setPrice(shippingMethodCreateDto.getPrice());
        
        ShippingMethod updatedShippingMethod = shippingMethodRepository.save(existingShippingMethod);
        return Optional.of(convertToGetDto(updatedShippingMethod));
    }

    @Override
    public void deleteShippingMethod(Long shippingMethodId) {
        if (!shippingMethodRepository.existsById(shippingMethodId)) {
            throw new ResourceNotFoundException("ShippingMethod not found with id: " + shippingMethodId);
        }
        shippingMethodRepository.deleteById(shippingMethodId);
    }

    private ShippingMethodGetDto convertToGetDto(ShippingMethod shippingMethod) {
        return ShippingMethodGetDto.builder()
                .id(shippingMethod.getId())
                .name(shippingMethod.getName())
                .price(shippingMethod.getPrice())
                .build();
    }
}