package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.Address.AddressCreateDto;
import com.example.wildlife_backend.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    Address createAddress(AddressCreateDto addressCreateDto, Long userId);
    
    Optional<Address> getAddressById(Long addressId);
    
    List<Address> getAllAddresses();
    
    Optional<Address> updateAddress(Long addressId, AddressCreateDto addressCreateDto);
    
    void deleteAddress(Long addressId);
}
