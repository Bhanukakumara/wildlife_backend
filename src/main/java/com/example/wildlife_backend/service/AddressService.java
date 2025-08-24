package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.Address.AddressCreateDto;
import com.example.wildlife_backend.dto.Address.AddressGetDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    AddressGetDto createAddress(AddressCreateDto addressCreateDto, Long userId);
    
    Optional<AddressGetDto> getAddressById(Long addressId);
    
    List<AddressGetDto> getAllAddresses();
    
    Optional<AddressGetDto> updateAddress(Long addressId, AddressCreateDto addressCreateDto);
    
    void deleteAddress(Long addressId);

    List<AddressGetDto> getAddressesByUserId(Long userId);

    List<AddressGetDto> getAddressesByType(String addressType);

    Optional<AddressGetDto> associateAddressWithOrder(Long addressId, Long orderId);

    boolean validateAddress(@Valid AddressCreateDto addressCreateDto);
}
