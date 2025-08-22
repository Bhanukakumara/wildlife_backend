package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.OrderLine.OrderLineCreateDto;
import com.example.wildlife_backend.dto.OrderLine.OrderLineGetDto;

import java.util.List;
import java.util.Optional;

public interface OrderLineService {
    OrderLineGetDto createOrderLine(OrderLineCreateDto orderLineCreateDto);
    Optional<OrderLineGetDto> getOrderLineById(Long orderLineId);
    List<OrderLineGetDto> getAllOrderLines();
    Optional<OrderLineGetDto> updateOrderLine(Long orderLineId, OrderLineCreateDto orderLineCreateDto);
    void deleteOrderLine(Long orderLineId);
}