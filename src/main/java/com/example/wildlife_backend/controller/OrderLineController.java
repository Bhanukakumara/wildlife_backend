package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.OrderLine.OrderLineCreateDto;
import com.example.wildlife_backend.dto.OrderLine.OrderLineGetDto;
import com.example.wildlife_backend.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-lines")
@CrossOrigin
@RequiredArgsConstructor
public class OrderLineController {
    private final OrderLineService orderLineService;

    // Create a new order line
    @PostMapping
    public ResponseEntity<OrderLineGetDto> createOrderLine(@RequestBody OrderLineCreateDto orderLineCreateDto) {
        return new ResponseEntity<>(orderLineService.createOrderLine(orderLineCreateDto), HttpStatus.CREATED);
    }

    // Get order line by ID
    @GetMapping("/{orderLineId}")
    public ResponseEntity<OrderLineGetDto> getOrderLineById(@PathVariable Long orderLineId) {
        Optional<OrderLineGetDto> orderLine = orderLineService.getOrderLineById(orderLineId);
        return orderLine.map(line -> new ResponseEntity<>(line, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all order lines
    @GetMapping
    public ResponseEntity<List<OrderLineGetDto>> getAllOrderLines() {
        List<OrderLineGetDto> orderLines = orderLineService.getAllOrderLines();
        if (orderLines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orderLines, HttpStatus.OK);
    }

    // Update order line
    @PutMapping("/{orderLineId}")
    public ResponseEntity<OrderLineGetDto> updateOrderLine(@PathVariable Long orderLineId, @RequestBody OrderLineCreateDto orderLineCreateDto) {
        Optional<OrderLineGetDto> updatedOrderLine = orderLineService.updateOrderLine(orderLineId, orderLineCreateDto);
        return updatedOrderLine.map(line -> new ResponseEntity<>(line, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete order line
    @DeleteMapping("/{orderLineId}")
    public ResponseEntity<Void> deleteOrderLine(@PathVariable Long orderLineId) {
        try {
            orderLineService.deleteOrderLine(orderLineId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}