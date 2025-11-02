package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.orderItem.OrderItemDto;
import com.example.wildlife_backend.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemDto> items = orderItemService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByPhotoId(@PathVariable Long photoId) {
        List<OrderItemDto> items = orderItemService.getOrderItemsByPhotoId(photoId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable Long id) {
        Optional<OrderItemDto> item = orderItemService.getOrderItemById(id);
        return item.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
}
