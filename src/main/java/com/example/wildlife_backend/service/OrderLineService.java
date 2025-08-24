package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.OrderLine.OrderLineCreateDto;
import com.example.wildlife_backend.dto.OrderLine.OrderLineGetDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface OrderLineService {
    OrderLineGetDto createOrderLine(OrderLineCreateDto orderLineCreateDto);
    Optional<OrderLineGetDto> getOrderLineById(Long orderLineId);
    List<OrderLineGetDto> getAllOrderLines();
    Optional<OrderLineGetDto> updateOrderLine(Long orderLineId, OrderLineCreateDto orderLineCreateDto);
    boolean deleteOrderLine(Long orderLineId);

    List<OrderLineGetDto> getOrderLinesByOrderId(Long orderId);

    List<OrderLineGetDto> getOrderLinesByProductItemId(Long productItemId);

    List<OrderLineGetDto> bulkCreateOrderLines(@Valid List<OrderLineCreateDto> orderLineCreateDtos);

    boolean validateOrderLine(@Valid OrderLineCreateDto orderLineCreateDto);

    List<OrderLineGetDto> getOrderLinesWithReviews();
}