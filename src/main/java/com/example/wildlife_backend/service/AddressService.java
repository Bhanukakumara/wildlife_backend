package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.AddressCreateDto;
import com.example.wildlife_backend.entity.Address;

public interface AddressService {
    Address createAddress(AddressCreateDto addressCreateDto, Long userId);
}
