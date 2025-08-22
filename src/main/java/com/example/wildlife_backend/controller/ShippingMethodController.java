package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.ShippingMethod.ShippingMethodCreateDto;
import com.example.wildlife_backend.dto.ShippingMethod.ShippingMethodGetDto;
import com.example.wildlife_backend.service.ShippingMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shipping-methods")
@CrossOrigin
@RequiredArgsConstructor
public class ShippingMethodController {
    private final ShippingMethodService shippingMethodService;

    // Create a new shipping method
    @PostMapping
    public ResponseEntity<ShippingMethodGetDto> createShippingMethod(@RequestBody ShippingMethodCreateDto shippingMethodCreateDto) {
        return new ResponseEntity<>(shippingMethodService.createShippingMethod(shippingMethodCreateDto), HttpStatus.CREATED);
    }

    // Get shipping method by ID
    @GetMapping("/{shippingMethodId}")
    public ResponseEntity<ShippingMethodGetDto> getShippingMethodById(@PathVariable Long shippingMethodId) {
        Optional<ShippingMethodGetDto> shippingMethod = shippingMethodService.getShippingMethodById(shippingMethodId);
        return shippingMethod.map(method -> new ResponseEntity<>(method, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all shipping methods
    @GetMapping
    public ResponseEntity<List<ShippingMethodGetDto>> getAllShippingMethods() {
        List<ShippingMethodGetDto> shippingMethods = shippingMethodService.getAllShippingMethods();
        if (shippingMethods.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shippingMethods, HttpStatus.OK);
    }

    // Update shipping method
    @PutMapping("/{shippingMethodId}")
    public ResponseEntity<ShippingMethodGetDto> updateShippingMethod(@PathVariable Long shippingMethodId, @RequestBody ShippingMethodCreateDto shippingMethodCreateDto) {
        Optional<ShippingMethodGetDto> updatedShippingMethod = shippingMethodService.updateShippingMethod(shippingMethodId, shippingMethodCreateDto);
        return updatedShippingMethod.map(method -> new ResponseEntity<>(method, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete shipping method
    @DeleteMapping("/{shippingMethodId}")
    public ResponseEntity<Void> deleteShippingMethod(@PathVariable Long shippingMethodId) {
        try {
            shippingMethodService.deleteShippingMethod(shippingMethodId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}