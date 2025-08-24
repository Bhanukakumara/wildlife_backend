package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.ShopOrder.ShopOrderCreateDto;
import com.example.wildlife_backend.dto.ShopOrder.ShopOrderGetDto;
import com.example.wildlife_backend.service.ShopOrderService;
import com.example.wildlife_backend.util.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shop-orders")
@CrossOrigin
@RequiredArgsConstructor
public class ShopOrderController {
    private final ShopOrderService shopOrderService;

    // Create a new shop order
    @PostMapping
    public ResponseEntity<ShopOrderGetDto> createShopOrder(@Valid @RequestBody ShopOrderCreateDto shopOrderCreateDto) {
        ShopOrderGetDto createdShopOrder = shopOrderService.createShopOrder(shopOrderCreateDto);
        return new ResponseEntity<>(createdShopOrder, HttpStatus.CREATED);
    }

    // Get shop order by ID
    @GetMapping("/{shopOrderId}")
    public ResponseEntity<ShopOrderGetDto> getShopOrderById(@PathVariable Long shopOrderId) {
        Optional<ShopOrderGetDto> shopOrder = shopOrderService.getShopOrderById(shopOrderId);
        return shopOrder.map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all shop orders
    @GetMapping
    public ResponseEntity<List<ShopOrderGetDto>> getAllShopOrders() {
        List<ShopOrderGetDto> shopOrders = shopOrderService.getAllShopOrders();
        if (shopOrders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shopOrders, HttpStatus.OK);
    }

    // Get shop orders by user payment method ID
    @GetMapping("/by-payment-method/{paymentMethodId}")
    public ResponseEntity<List<ShopOrderGetDto>> getShopOrdersByPaymentMethodId(@PathVariable Long paymentMethodId) {
        List<ShopOrderGetDto> shopOrders = shopOrderService.getShopOrdersByPaymentMethodId(paymentMethodId);
        if (shopOrders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shopOrders, HttpStatus.OK);
    }

    // Get shop orders by shipping address ID
    @GetMapping("/by-address/{addressId}")
    public ResponseEntity<List<ShopOrderGetDto>> getShopOrdersByAddressId(@PathVariable Long addressId) {
        List<ShopOrderGetDto> shopOrders = shopOrderService.getShopOrdersByAddressId(addressId);
        if (shopOrders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shopOrders, HttpStatus.OK);
    }

    // Get shop orders by shipping method ID
    @GetMapping("/by-shipping-method/{shippingMethodId}")
    public ResponseEntity<List<ShopOrderGetDto>> getShopOrdersByShippingMethodId(@PathVariable Long shippingMethodId) {
        List<ShopOrderGetDto> shopOrders = shopOrderService.getShopOrdersByShippingMethodId(shippingMethodId);
        if (shopOrders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shopOrders, HttpStatus.OK);
    }

    // Get shop orders by order status
    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<ShopOrderGetDto>> getShopOrdersByStatus(@PathVariable OrderStatus status) {
        List<ShopOrderGetDto> shopOrders = shopOrderService.getShopOrdersByStatus(status);
        if (shopOrders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shopOrders, HttpStatus.OK);
    }

    // Get shop orders by date range
    @GetMapping("/by-date-range")
    public ResponseEntity<List<ShopOrderGetDto>> getShopOrdersByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<ShopOrderGetDto> shopOrders = shopOrderService.getShopOrdersByDateRange(startDate, endDate);
        if (shopOrders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shopOrders, HttpStatus.OK);
    }

    // Update shop order
    @PutMapping("/{shopOrderId}")
    public ResponseEntity<ShopOrderGetDto> updateShopOrder(
            @PathVariable Long shopOrderId,
            @Valid @RequestBody ShopOrderCreateDto shopOrderCreateDto) {
        Optional<ShopOrderGetDto> updatedShopOrder = shopOrderService.updateShopOrder(shopOrderId, shopOrderCreateDto);
        return updatedShopOrder.map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete shop order
    @DeleteMapping("/{shopOrderId}")
    public ResponseEntity<Boolean> deleteShopOrder(@PathVariable Long shopOrderId) {
        boolean deleted = shopOrderService.deleteShopOrder(shopOrderId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Bulk create shop orders
    @PostMapping("/bulk-create")
    public ResponseEntity<List<ShopOrderGetDto>> bulkCreateShopOrders(
            @Valid @RequestBody List<ShopOrderCreateDto> shopOrderCreateDtos) {
        List<ShopOrderGetDto> createdShopOrders = shopOrderService.bulkCreateShopOrders(shopOrderCreateDtos);
        return new ResponseEntity<>(createdShopOrders, HttpStatus.CREATED);
    }

    // Validate shop order data
    @PostMapping("/validate")
    public ResponseEntity<String> validateShopOrder(@Valid @RequestBody ShopOrderCreateDto shopOrderCreateDto) {
        boolean isValid = shopOrderService.validateShopOrder(shopOrderCreateDto);
        if (isValid) {
            return new ResponseEntity<>("Shop order data is valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid shop order data", HttpStatus.BAD_REQUEST);
    }

    // Get shop orders with order lines
    @GetMapping("/with-order-lines")
    public ResponseEntity<List<ShopOrderGetDto>> getShopOrdersWithOrderLines() {
        List<ShopOrderGetDto> shopOrders = shopOrderService.getShopOrdersWithOrderLines();
        if (shopOrders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(shopOrders, HttpStatus.OK);
    }
}