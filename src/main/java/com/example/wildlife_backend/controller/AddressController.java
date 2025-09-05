package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.Address.AddressCreateDto;
import com.example.wildlife_backend.dto.Address.AddressGetDto;
import com.example.wildlife_backend.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/address")
@CrossOrigin
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    //Create a new Address for a User
    @PostMapping("/create/{userId}")
    public ResponseEntity<AddressGetDto> createAddress(@PathVariable Long userId, @RequestBody AddressCreateDto addressCreateDto){
        AddressGetDto addressGetDto = addressService.createAddress(addressCreateDto, userId);
        if (addressGetDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(addressGetDto, HttpStatus.CREATED);
        }
    }

    // Get address by ID
    @GetMapping("/get-by-id/{addressId}")
    public ResponseEntity<AddressGetDto> getAddressById(@PathVariable Long addressId) {
        AddressGetDto addressById = addressService.getAddressById(addressId);
        if (addressById == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(addressById, HttpStatus.OK);
        }
    }

    // Get all addresses for a specific user
    @GetMapping("/get-by-user/{userId}")
    public ResponseEntity<List<AddressGetDto>> getAddressesByUserId(@PathVariable Long userId) {
        List<AddressGetDto> userAddresses = addressService.getAddressesByUserId(userId);
        if (userAddresses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userAddresses, HttpStatus.OK);
    }

    // Get addresses by address type (e.g., billing or shipping)
    @GetMapping("/get-by-type/{addressType}")
    public ResponseEntity<List<AddressGetDto>> getAddressesByType(@PathVariable String addressType) {
        List<AddressGetDto> addressesByType = addressService.getAddressesByType(addressType);
        if (addressesByType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(addressesByType, HttpStatus.OK);
    }

    // Get all addresses
    @GetMapping("/get-all")
    public ResponseEntity<List<AddressGetDto>> getAllAddresses() {
        List<AddressGetDto> allAddresses = addressService.getAllAddresses();
        if (allAddresses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allAddresses, HttpStatus.OK);
    }

    // Update an existing address
    @PutMapping("/update/{addressId}")
    public ResponseEntity<AddressGetDto> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressCreateDto addressCreateDto) {
        AddressGetDto updatedAddress = addressService.updateAddress(addressId, addressCreateDto);
        if (updatedAddress == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
        }
    }

    // Associate an address with a shop order
    @PostMapping("/associate-order/{addressId}/{orderId}")
    public ResponseEntity<AddressGetDto> associateAddressWithOrder(
            @PathVariable Long addressId,
            @PathVariable Long orderId) {
        Optional<AddressGetDto> associatedAddress = addressService.associateAddressWithOrder(addressId, orderId);
        return associatedAddress.map(address -> new ResponseEntity<>(address, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
