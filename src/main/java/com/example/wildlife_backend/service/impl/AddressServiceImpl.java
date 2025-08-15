package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.AddressCreateDto;
import com.example.wildlife_backend.entity.Address;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.entity.UserAddress;
import com.example.wildlife_backend.repository.AddressRepository;
import com.example.wildlife_backend.repository.UserAddressRepository;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    @Override
    public Address createAddress(AddressCreateDto addressCreateDto, Long userId) {
        if (addressCreateDto == null) {
            throw new IllegalArgumentException("addressCreateDto cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        // Create Address entity
        Address address = Address.builder()
                .unitNumber(addressCreateDto.getUnitNumber())
                .streetNumber(addressCreateDto.getStreetNumber())
                .addressLine1(addressCreateDto.getAddressLine1())
                .addressLine2(addressCreateDto.getAddressLine2())
                .city(addressCreateDto.getCity())
                .stateProvince(addressCreateDto.getStateProvince())
                .postalCode(addressCreateDto.getPostalCode())
                .addressType(addressCreateDto.getAddressType())
                .deliveryInstructions(addressCreateDto.getDeliveryInstructions())
                .country(addressCreateDto.getCountry())
                .build();

        // Save Address
        Address savedAddress = addressRepository.save(address);

        // Create and save UserAddress with the saved Address
        UserAddress userAddress = UserAddress.builder()
                .user(user.get())
                .address(savedAddress)
                .isDefault(addressCreateDto.isDefault())
                .build();
        userAddressRepository.save(userAddress);

        return address;
    }
}
