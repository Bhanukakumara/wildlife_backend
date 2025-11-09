package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.order.OrderCreateDto;
import com.example.wildlife_backend.dto.order.OrderResponseDto;
import com.example.wildlife_backend.dto.order.OrderUpdateDto;
import com.example.wildlife_backend.dto.orderItem.OrderItemDto;
import com.example.wildlife_backend.entity.Order;
import com.example.wildlife_backend.util.OrderStatus;
import com.example.wildlife_backend.entity.OrderItem;
import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.repository.OrderRepository;
import com.example.wildlife_backend.repository.UserRepository;
import com.example.wildlife_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponseDto createOrder(OrderCreateDto orderDto) {
        User user = userRepository.findById(orderDto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(orderDto.getOrderNumber());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setCurrency(orderDto.getCurrency());

        // Set billing information
        order.setBillingName(orderDto.getBillingName());
        order.setBillingEmail(orderDto.getBillingEmail());
        order.setBillingPhone(orderDto.getBillingPhone());
        order.setBillingAddress(orderDto.getBillingAddress());
        order.setBillingCity(orderDto.getBillingCity());
        order.setBillingState(orderDto.getBillingState());
        order.setBillingZip(orderDto.getBillingZip());
        order.setBillingCountry(orderDto.getBillingCountry());

        // Set shipping information
        order.setShippingName(orderDto.getShippingName());
        order.setShippingPhone(orderDto.getShippingPhone());
        order.setShippingAddress(orderDto.getShippingAddress());
        order.setShippingCity(orderDto.getShippingCity());
        order.setShippingState(orderDto.getShippingState());
        order.setShippingZip(orderDto.getShippingZip());
        order.setShippingCountry(orderDto.getShippingCountry());

        order.setNotes(orderDto.getNotes());

        // Create order items from DTOs
        if (orderDto.getItems() != null) {
            // This would need to be implemented - for now create empty order
            // In a real implementation, you'd convert OrderItemCreateDto to OrderItem entities
        }

        Order savedOrder = createOrderEntity(order);
        return convertToOrderResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto updateOrder(Long id, OrderUpdateDto orderDto) {
        Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        // Update allowed fields based on current status
        if (existingOrder.getStatus() == OrderStatus.PENDING ||
            existingOrder.getStatus() == OrderStatus.PROCESSING) {

            if (orderDto.getBillingName() != null) existingOrder.setBillingName(orderDto.getBillingName());
            if (orderDto.getBillingEmail() != null) existingOrder.setBillingEmail(orderDto.getBillingEmail());
            if (orderDto.getBillingPhone() != null) existingOrder.setBillingPhone(orderDto.getBillingPhone());
            if (orderDto.getBillingAddress() != null) existingOrder.setBillingAddress(orderDto.getBillingAddress());
            if (orderDto.getBillingCity() != null) existingOrder.setBillingCity(orderDto.getBillingCity());
            if (orderDto.getBillingState() != null) existingOrder.setBillingState(orderDto.getBillingState());
            if (orderDto.getBillingZip() != null) existingOrder.setBillingZip(orderDto.getBillingZip());
            if (orderDto.getBillingCountry() != null) existingOrder.setBillingCountry(orderDto.getBillingCountry());

            if (orderDto.getShippingName() != null) existingOrder.setShippingName(orderDto.getShippingName());
            if (orderDto.getShippingPhone() != null) existingOrder.setShippingPhone(orderDto.getShippingPhone());
            if (orderDto.getShippingAddress() != null) existingOrder.setShippingAddress(orderDto.getShippingAddress());
            if (orderDto.getShippingCity() != null) existingOrder.setShippingCity(orderDto.getShippingCity());
            if (orderDto.getShippingState() != null) existingOrder.setShippingState(orderDto.getShippingState());
            if (orderDto.getShippingZip() != null) existingOrder.setShippingZip(orderDto.getShippingZip());
            if (orderDto.getShippingCountry() != null) existingOrder.setShippingCountry(orderDto.getShippingCountry());

            if (orderDto.getNotes() != null) existingOrder.setNotes(orderDto.getNotes());

            calculateOrderTotal(existingOrder);
        }

        Order savedOrder = orderRepository.save(existingOrder);
        return convertToOrderResponseDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderResponseDto> getOrderById(Long id) {
        return orderRepository.findById(id)
            .map(this::convertToOrderResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderResponseDto> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
            .map(this::convertToOrderResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(this::convertToOrderResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        // Only allow deletion of pending orders
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Cannot delete order that is not pending");
        }

        orderRepository.deleteById(id);
    }

    @Override
    public OrderResponseDto placeOrder(OrderCreateDto orderDto) {
        OrderResponseDto order = createOrder(orderDto);
        // Set status to PENDING - this would be done in createOrderEntity
        return order;
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus currentStatus = order.getStatus();

        // Validate status transitions
        if (!isValidStatusTransition(currentStatus, status)) {
            throw new RuntimeException("Invalid status transition from " + currentStatus + " to " + status);
        }

        order.setStatus(status);

        // Set timestamps based on status
        LocalDateTime now = LocalDateTime.now();
        switch (status) {
            case PENDING:
            case PROCESSING:
                // Status transition timestamp if needed
                break;
            case COMPLETED:
                if (order.getDeliveredAt() == null) {
                    order.setDeliveredAt(now);
                }
                break;
            case CANCELLED:
                order.setCancelledAt(now);
                break;
            case REFUNDED:
                order.setRefundedAt(now);
                break;
            case FAILED:
                // No timestamp change defined for failed orders yet
                break;
        }

        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto cancelOrder(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PROCESSING) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(reason);
        order.setCancelledAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto shipOrder(Long orderId, String trackingNumber) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PROCESSING) {
            throw new RuntimeException("Order must be in PROCESSING status to ship");
        }

        order.setStatus(OrderStatus.PROCESSING); // Keep as processing until delivered
        order.setTrackingNumber(trackingNumber);
        order.setShippedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PROCESSING) {
            throw new RuntimeException("Order must be in PROCESSING status to deliver");
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setDeliveredAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto refundOrder(Long orderId, BigDecimal refundAmount) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new RuntimeException("Only completed orders can be refunded");
        }

        if (refundAmount.compareTo(order.getTotalAmount()) > 0) {
            throw new RuntimeException("Refund amount cannot exceed order total");
        }

        order.setStatus(OrderStatus.REFUNDED);
        order.setRefundAmount(refundAmount);
        order.setRefundedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponseDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByUser(Long userId) {
        return orderRepository.findOrdersByUserId(userId).stream()
            .map(this::convertToOrderResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findOrdersByStatus(status).stream()
            .map(this::convertToOrderResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getPendingOrders() {
        return orderRepository.findPendingOrders().stream()
            .map(this::convertToOrderResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getProcessingOrders() {
        return orderRepository.findProcessingOrders().stream()
            .map(this::convertToOrderResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto addOrderItem(Long orderId, OrderItem item) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Cannot modify order items for non-pending orders");
        }

        order.addOrderItem(item);
        calculateOrderTotal(order);

        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto removeOrderItem(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Cannot modify order items for non-pending orders");
        }

        OrderItem itemToRemove = order.getItems().stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Order item not found"));

        order.removeOrderItem(itemToRemove);
        calculateOrderTotal(order);

        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponseDto(savedOrder);
    }

    @Override
    public BigDecimal getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal revenue = orderRepository.getTotalRevenueBetweenDates(startDate, endDate);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @Override
    public long getOrderCount(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);
        return orders.size();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOrderNumberAvailable(String orderNumber) {
        return !orderRepository.existsByOrderNumber(orderNumber);
    }

    private Order createOrderEntity(Order order) {
        validateOrder(order);
        calculateOrderTotal(order);
        return orderRepository.save(order);
    }

    private OrderResponseDto convertToOrderResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setUserId(order.getUser().getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setSubtotalAmount(order.getSubtotalAmount());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setShippingAmount(order.getShippingAmount());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setCurrency(order.getCurrency());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setPaymentId(order.getPaymentId());
        dto.setTransactionId(order.getTransactionId());
        dto.setBillingName(order.getBillingName());
        dto.setBillingEmail(order.getBillingEmail());
        dto.setBillingPhone(order.getBillingPhone());
        dto.setBillingAddress(order.getBillingAddress());
        dto.setBillingCity(order.getBillingCity());
        dto.setBillingState(order.getBillingState());
        dto.setBillingZip(order.getBillingZip());
        dto.setBillingCountry(order.getBillingCountry());
        dto.setShippingName(order.getShippingName());
        dto.setShippingPhone(order.getShippingPhone());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setShippingCity(order.getShippingCity());
        dto.setShippingState(order.getShippingState());
        dto.setShippingZip(order.getShippingZip());
        dto.setShippingCountry(order.getShippingCountry());
        dto.setNotes(order.getNotes());
        dto.setTrackingNumber(order.getTrackingNumber());
        dto.setShippedAt(order.getShippedAt());
        dto.setDeliveredAt(order.getDeliveredAt());
        dto.setCancelledAt(order.getCancelledAt());
        dto.setCancellationReason(order.getCancellationReason());
        dto.setRefundedAt(order.getRefundedAt());
        dto.setRefundAmount(order.getRefundAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        // Convert items to DTOs
        dto.setItems(order.getItems().stream()
            .map(this::convertToOrderItemDto)
            .collect(Collectors.toList()));
        return dto;
    }

    private OrderItemDto convertToOrderItemDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setPhotoId(item.getPhoto().getId());
        dto.setPhotoTitle(item.getPhoto().getTitle());
        dto.setPhotoImageUrl(item.getPhoto().getImageUrl());
        dto.setLicenseType(item.getLicenseType());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setTotalPrice(item.getTotalPrice());
        dto.setLicensePrice(item.getLicensePrice());
        dto.setDownloadUrl(item.getDownloadUrl());
        dto.setDownloadsRemaining(item.getDownloadsRemaining());
        dto.setDownloadExpiryDays(item.getDownloadExpiryDays());
        dto.setExpiresAt(item.getExpiresAt());
        dto.setIsDownloaded(item.getIsDownloaded());
        dto.setDownloadedAt(item.getDownloadedAt());
        dto.setDownloadCount(item.getDownloadCount());
        return dto;
    }

    private void validateOrder(Order order) {
        if (order.getUser() == null) {
            throw new RuntimeException("Order must have a user");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("Order must have at least one item");
        }
        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Order total must be greater than zero");
        }
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateOrderTotal(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal subtotal = order.getItems().stream()
            .map(OrderItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setSubtotalAmount(subtotal);

        BigDecimal total = subtotal
            .add(order.getTaxAmount() != null ? order.getTaxAmount() : BigDecimal.ZERO)
            .add(order.getShippingAmount() != null ? order.getShippingAmount() : BigDecimal.ZERO)
            .subtract(order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO);

        order.setTotalAmount(total);
        return total;
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        switch (current) {
            case PENDING:
                return next == OrderStatus.PROCESSING || next == OrderStatus.CANCELLED;
            case PROCESSING:
                return next == OrderStatus.COMPLETED || next == OrderStatus.CANCELLED;
            case COMPLETED:
                return next == OrderStatus.REFUNDED;
            case CANCELLED:
            case REFUNDED:
            case FAILED:
                return false; // Terminal states
            default:
                return false;
        }
    }
}
