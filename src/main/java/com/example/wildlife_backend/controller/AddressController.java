package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.AddressCreateDto;
import com.example.wildlife_backend.entity.Address;
import com.example.wildlife_backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
