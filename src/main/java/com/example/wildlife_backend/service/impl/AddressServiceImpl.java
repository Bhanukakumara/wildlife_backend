package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.Address.AddressCreateDto;
import com.example.wildlife_backend.dto.Address.AddressGetDto;
import com.example.wildlife_backend.entity.Address;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.entity.UserAddress;
import com.example.wildlife_backend.repository.AddressRepository;
import com.example.wildlife_backend.repository.UserAddressRepository;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    @Override
    public AddressGetDto createAddress(AddressCreateDto addressCreateDto, Long userId) {
        if (addressCreateDto == null) {
            throw new IllegalArgumentException("addressCreateDto cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
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

        // Create and save UserAddress
        UserAddress userAddress = UserAddress.builder()
                .user(userOpt.get())
                .address(savedAddress)
                .isDefault(addressCreateDto.isDefault())
                .build();
        userAddressRepository.save(userAddress);

        return convertAddressToDto(savedAddress);
    }

    @Override
    public Optional<AddressGetDto> getAddressById(Long addressId) {
        if (addressId == null) {
            throw new IllegalArgumentException("addressId cannot be null");
        }
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(convertAddressToDto(addressOpt.get()));
    }

    @Override
    public List<AddressGetDto> getAllAddresses() {
        List<Address> addressList = addressRepository.findAll();
        List<AddressGetDto> addressGetDtoList = new ArrayList<>();
        addressList.forEach(address -> addressGetDtoList.add(convertAddressToDto(address)));
        return addressGetDtoList;
    }

    @Override
    public Optional<AddressGetDto> updateAddress(Long addressId, AddressCreateDto addressCreateDto) {
        if (addressId == null) {
            throw new IllegalArgumentException("addressId cannot be null");
        }
        if (addressCreateDto == null) {
            throw new IllegalArgumentException("addressCreateDto cannot be null");
        }

        Optional<Address> existingAddressOpt = addressRepository.findById(addressId);
        if (existingAddressOpt.isEmpty()) {
            return Optional.empty();
        }

        Address existingAddress = existingAddressOpt.get();
        existingAddress.setUnitNumber(addressCreateDto.getUnitNumber());
        existingAddress.setStreetNumber(addressCreateDto.getStreetNumber());
        existingAddress.setAddressLine1(addressCreateDto.getAddressLine1());
        existingAddress.setAddressLine2(addressCreateDto.getAddressLine2());
        existingAddress.setCity(addressCreateDto.getCity());
        existingAddress.setStateProvince(addressCreateDto.getStateProvince());
        existingAddress.setPostalCode(addressCreateDto.getPostalCode());
        existingAddress.setAddressType(addressCreateDto.getAddressType());
        existingAddress.setDeliveryInstructions(addressCreateDto.getDeliveryInstructions());
        existingAddress.setCountry(addressCreateDto.getCountry());

        Address updatedAddress = addressRepository.save(existingAddress);
        return Optional.of(convertAddressToDto(updatedAddress));
    }

    @Override
    public void deleteAddress(Long addressId) {
        if (addressId == null) {
            throw new IllegalArgumentException("addressId cannot be null");
        }

        userAddressRepository.deleteByAddressId(addressId);
        addressRepository.deleteById(addressId);
    }

    private AddressGetDto convertAddressToDto(Address address) {
        return AddressGetDto.builder()
                .id(address.getId())
                .unitNumber(address.getUnitNumber())
                .streetNumber(address.getStreetNumber())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .stateProvince(address.getStateProvince())
                .postalCode(address.getPostalCode())
                .addressType(address.getAddressType())
                .deliveryInstructions(address.getDeliveryInstructions())
                .country(address.getCountry().getName())
                .build();
    }
}
