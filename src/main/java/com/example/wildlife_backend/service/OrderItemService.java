package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.orderItem.OrderItemDto;

import java.util.List;
import java.util.Optional;

public interface OrderItemService {
    List<OrderItemDto> getOrderItemsByOrderId(Long orderId);
    List<OrderItemDto> getOrderItemsByPhotoId(Long photoId);
    Optional<OrderItemDto> getOrderItemById(Long id);
}
