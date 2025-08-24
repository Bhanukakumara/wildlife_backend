package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.OrderLine.OrderLineCreateDto;
import com.example.wildlife_backend.dto.OrderLine.OrderLineGetDto;
import com.example.wildlife_backend.service.OrderLineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-line")
@CrossOrigin
@RequiredArgsConstructor
public class OrderLineController {
    private final OrderLineService orderLineService;

    // Create a new order line
    @PostMapping
    public ResponseEntity<OrderLineGetDto> createOrderLine(@Valid @RequestBody OrderLineCreateDto orderLineCreateDto) {
        OrderLineGetDto createdOrderLine = orderLineService.createOrderLine(orderLineCreateDto);
        return new ResponseEntity<>(createdOrderLine, HttpStatus.CREATED);
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

    // Get order lines by shop order ID
    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<List<OrderLineGetDto>> getOrderLinesByOrderId(@PathVariable Long orderId) {
        List<OrderLineGetDto> orderLines = orderLineService.getOrderLinesByOrderId(orderId);
        if (orderLines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orderLines, HttpStatus.OK);
    }

    // Get order lines by product item ID
    @GetMapping("/by-product/{productItemId}")
    public ResponseEntity<List<OrderLineGetDto>> getOrderLinesByProductItemId(@PathVariable Long productItemId) {
        List<OrderLineGetDto> orderLines = orderLineService.getOrderLinesByProductItemId(productItemId);
        if (orderLines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orderLines, HttpStatus.OK);
    }

    // Update order line
    @PutMapping("/{orderLineId}")
    public ResponseEntity<OrderLineGetDto> updateOrderLine(
            @PathVariable Long orderLineId,
            @Valid @RequestBody OrderLineCreateDto orderLineCreateDto) {
        Optional<OrderLineGetDto> updatedOrderLine = orderLineService.updateOrderLine(orderLineId, orderLineCreateDto);
        return updatedOrderLine.map(line -> new ResponseEntity<>(line, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete order line
    @DeleteMapping("/{orderLineId}")
    public ResponseEntity<Void> deleteOrderLine(@PathVariable Long orderLineId) {
        boolean deleted = orderLineService.deleteOrderLine(orderLineId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Bulk create order lines
    @PostMapping("/bulk-create")
    public ResponseEntity<List<OrderLineGetDto>> bulkCreateOrderLines(
            @Valid @RequestBody List<OrderLineCreateDto> orderLineCreateDos) {
        List<OrderLineGetDto> createdOrderLines = orderLineService.bulkCreateOrderLines(orderLineCreateDos);
        return new ResponseEntity<>(createdOrderLines, HttpStatus.CREATED);
    }

    // Validate order line data
    @PostMapping("/validate")
    public ResponseEntity<String> validateOrderLine(@Valid @RequestBody OrderLineCreateDto orderLineCreateDto) {
        boolean isValid = orderLineService.validateOrderLine(orderLineCreateDto);
        if (isValid) {
            return new ResponseEntity<>("Order line data is valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid order line data", HttpStatus.BAD_REQUEST);
    }

    // Get order lines with associated reviews
    @GetMapping("/with-reviews")
    public ResponseEntity<List<OrderLineGetDto>> getOrderLinesWithReviews() {
        List<OrderLineGetDto> orderLines = orderLineService.getOrderLinesWithReviews();
        if (orderLines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orderLines, HttpStatus.OK);
    }
}