package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.OrderLine.OrderLineCreateDto;
import com.example.wildlife_backend.dto.OrderLine.OrderLineGetDto;
import com.example.wildlife_backend.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-lines")
@RequiredArgsConstructor
public class OrderLineController {
    private final OrderLineService orderLineService;

    @PostMapping
    public ResponseEntity<OrderLineGetDto> createOrderLine(@RequestBody OrderLineCreateDto orderLineCreateDto) {
        OrderLineGetDto createdOrderLine = orderLineService.createOrderLine(orderLineCreateDto);
        return ResponseEntity.ok(createdOrderLine);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderLineGetDto> getOrderLineById(@PathVariable Long id) {
        return orderLineService.getOrderLineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderLineGetDto>> getAllOrderLines() {
        List<OrderLineGetDto> orderLines = orderLineService.getAllOrderLines();
        return ResponseEntity.ok(orderLines);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderLineGetDto> updateOrderLine(@PathVariable Long id, @RequestBody OrderLineCreateDto orderLineCreateDto) {
        return orderLineService.updateOrderLine(id, orderLineCreateDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderLine(@PathVariable Long id) {
        if (orderLineService.deleteOrderLine(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
