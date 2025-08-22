package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.ShopOrder.ShopOrderCreateDto;
import com.example.wildlife_backend.dto.ShopOrder.ShopOrderGetDto;
import com.example.wildlife_backend.service.ShopOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ShopOrderGetDto> createShopOrder(@RequestBody ShopOrderCreateDto shopOrderCreateDto) {
        return new ResponseEntity<>(shopOrderService.createShopOrder(shopOrderCreateDto), HttpStatus.CREATED);
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

    // Update shop order
    @PutMapping("/{shopOrderId}")
    public ResponseEntity<ShopOrderGetDto> updateShopOrder(@PathVariable Long shopOrderId, @RequestBody ShopOrderCreateDto shopOrderCreateDto) {
        Optional<ShopOrderGetDto> updatedShopOrder = shopOrderService.updateShopOrder(shopOrderId, shopOrderCreateDto);
        return updatedShopOrder.map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete shop order
    @DeleteMapping("/{shopOrderId}")
    public ResponseEntity<Void> deleteShopOrder(@PathVariable Long shopOrderId) {
        try {
            shopOrderService.deleteShopOrder(shopOrderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}