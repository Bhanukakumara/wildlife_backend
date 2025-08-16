package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.Address.AddressCreateDto;
import com.example.wildlife_backend.entity.Address;
import com.example.wildlife_backend.service.AddressService;
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

    @PostMapping("create/{userId}")
    public ResponseEntity<Address> createAddress(@RequestBody AddressCreateDto addressCreateDto, @PathVariable Long userId) {
        return new ResponseEntity<>(addressService.createAddress(addressCreateDto, userId), HttpStatus.CREATED);
    }

    // Get address by ID
    @GetMapping("/get-by-id/{addressId}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long addressId) {
        Optional<Address> addressById = addressService.getAddressById(addressId);
        return addressById.map(address -> new ResponseEntity<>(address, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all addresses
    @GetMapping("/get-all")
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> allAddresses = addressService.getAllAddresses();
        if (allAddresses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allAddresses, HttpStatus.OK);
    }

    // Update address
    @PutMapping("/update/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressId, @RequestBody AddressCreateDto addressCreateDto) {
        Optional<Address> updatedAddress = addressService.updateAddress(addressId, addressCreateDto);
        return updatedAddress.map(address -> new ResponseEntity<>(address, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete address
    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
