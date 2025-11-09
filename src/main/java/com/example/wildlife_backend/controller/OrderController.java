package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.order.OrderCreateDto;
import com.example.wildlife_backend.dto.order.OrderResponseDto;
import com.example.wildlife_backend.dto.order.OrderUpdateDto;
import com.example.wildlife_backend.entity.OrderItem;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.util.OrderStatus;
import com.example.wildlife_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Customer endpoints
    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody OrderCreateDto orderDto) {
        OrderResponseDto createdOrder = orderService.placeOrder(orderDto);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(Authentication authentication) {
        // Extract user ID from authentication
        Long userId = ((User) authentication.getPrincipal()).getId();
        List<OrderResponseDto> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        Optional<OrderResponseDto> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<OrderResponseDto> getOrderByOrderNumber(@PathVariable String orderNumber) {
        Optional<OrderResponseDto> order = orderService.getOrderByOrderNumber(orderNumber);
        return order.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long id, @RequestBody OrderUpdateDto orderDto) {
        OrderResponseDto updatedOrder = orderService.updateOrder(id, orderDto);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId, @RequestParam String reason) {
        OrderResponseDto cancelledOrder = orderService.cancelOrder(orderId, reason);
        return ResponseEntity.ok(cancelledOrder);
    }

    // Order items management
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponseDto> addOrderItem(@PathVariable Long orderId, @RequestBody OrderItem item) {
        OrderResponseDto updatedOrder = orderService.addOrderItem(orderId, item);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderResponseDto> removeOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        OrderResponseDto updatedOrder = orderService.removeOrderItem(orderId, itemId);
        return ResponseEntity.ok(updatedOrder);
    }

    // Admin endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/status/{status}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderResponseDto> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/pending")
    public ResponseEntity<List<OrderResponseDto>> getPendingOrders() {
        List<OrderResponseDto> orders = orderService.getPendingOrders();
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/processing")
    public ResponseEntity<List<OrderResponseDto>> getProcessingOrders() {
        List<OrderResponseDto> orders = orderService.getProcessingOrders();
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/ship")
    public ResponseEntity<OrderResponseDto> shipOrder(@PathVariable Long id, @RequestParam String trackingNumber) {
        OrderResponseDto shippedOrder = orderService.shipOrder(id, trackingNumber);
        return ResponseEntity.ok(shippedOrder);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/deliver")
    public ResponseEntity<OrderResponseDto> deliverOrder(@PathVariable Long id) {
        OrderResponseDto deliveredOrder = orderService.deliverOrder(id);
        return ResponseEntity.ok(deliveredOrder);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/refund")
    public ResponseEntity<OrderResponseDto> refundOrder(@PathVariable Long id, @RequestParam BigDecimal refundAmount) {
        OrderResponseDto refundedOrder = orderService.refundOrder(id, refundAmount);
        return ResponseEntity.ok(refundedOrder);
    }

    // Statistics endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        BigDecimal revenue = orderService.getTotalRevenue(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/count")
    public ResponseEntity<Long> getOrderCount(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        long count = orderService.getOrderCount(startDate, endDate);
        return ResponseEntity.ok(count);
    }

    // Validation endpoints
    @GetMapping("/validate/order-number")
    public ResponseEntity<Boolean> checkOrderNumberAvailability(@RequestParam String orderNumber) {
        boolean available = orderService.isOrderNumberAvailable(orderNumber);
        return ResponseEntity.ok(available);
    }
}
