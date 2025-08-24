package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.ShippingMethod.ShippingMethodCreateDto;
import com.example.wildlife_backend.dto.ShippingMethod.ShippingMethodGetDto;
import com.example.wildlife_backend.service.ShippingMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public ResponseEntity<ShippingMethodGetDto> createShippingMethod(@Valid @RequestBody ShippingMethodCreateDto shippingMethodCreateDto) {
        ShippingMethodGetDto createdShippingMethod = shippingMethodService.createShippingMethod(shippingMethodCreateDto);
        return new ResponseEntity<>(createdShippingMethod, HttpStatus.CREATED);
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

    // Search shipping methods by name (partial match)
    @GetMapping("/search")
    public ResponseEntity<List<ShippingMethodGetDto>> searchShippingMethodsByName(@RequestParam String name) {
        List<ShippingMethodGetDto> shippingMethods = shippingMethodService.searchShippingMethodsByName(name);
        if (shippingMethods.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shippingMethods, HttpStatus.OK);
    }

    // Get shipping methods by price range
    @GetMapping("/by-price-range")
    public ResponseEntity<List<ShippingMethodGetDto>> getShippingMethodsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ShippingMethodGetDto> shippingMethods = shippingMethodService.getShippingMethodsByPriceRange(minPrice, maxPrice);
        if (shippingMethods.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shippingMethods, HttpStatus.OK);
    }

    // Update shipping method
    @PutMapping("/{shippingMethodId}")
    public ResponseEntity<ShippingMethodGetDto> updateShippingMethod(
            @PathVariable Long shippingMethodId,
            @Valid @RequestBody ShippingMethodCreateDto shippingMethodCreateDto) {
        Optional<ShippingMethodGetDto> updatedShippingMethod = shippingMethodService.updateShippingMethod(shippingMethodId, shippingMethodCreateDto);
        return updatedShippingMethod.map(method -> new ResponseEntity<>(method, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete shipping method
    @DeleteMapping("/{shippingMethodId}")
    public ResponseEntity<Boolean> deleteShippingMethod(@PathVariable Long shippingMethodId) {
        boolean deleted = shippingMethodService.deleteShippingMethod(shippingMethodId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Bulk create shipping methods
    @PostMapping("/bulk-create")
    public ResponseEntity<List<ShippingMethodGetDto>> bulkCreateShippingMethods(
            @Valid @RequestBody List<ShippingMethodCreateDto> shippingMethodCreateDtos) {
        List<ShippingMethodGetDto> createdShippingMethods = shippingMethodService.bulkCreateShippingMethods(shippingMethodCreateDtos);
        return new ResponseEntity<>(createdShippingMethods, HttpStatus.CREATED);
    }

    // Validate shipping method data
    @PostMapping("/validate")
    public ResponseEntity<String> validateShippingMethod(@Valid @RequestBody ShippingMethodCreateDto shippingMethodCreateDto) {
        boolean isValid = shippingMethodService.validateShippingMethod(shippingMethodCreateDto);
        if (isValid) {
            return new ResponseEntity<>("Shipping method data is valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid shipping method data", HttpStatus.BAD_REQUEST);
    }

    // Get shipping methods with associated shop orders
    @GetMapping("/with-shop-orders")
    public ResponseEntity<List<ShippingMethodGetDto>> getShippingMethodsWithShopOrders() {
        List<ShippingMethodGetDto> shippingMethods = shippingMethodService.getShippingMethodsWithShopOrders();
        if (shippingMethods.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shippingMethods, HttpStatus.OK);
    }
}