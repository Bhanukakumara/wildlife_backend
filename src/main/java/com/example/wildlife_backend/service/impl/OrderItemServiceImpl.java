package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.orderItem.OrderItemDto;
import com.example.wildlife_backend.entity.OrderItem;
import com.example.wildlife_backend.repository.OrderItemRepository;
import com.example.wildlife_backend.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        // This would need Order repository - for now returning all
        return orderItemRepository.findAll().stream()
            .map(this::convertToOrderItemDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemDto> getOrderItemsByPhotoId(Long photoId) {
        // This would need Photo repository - for now returning all
        return orderItemRepository.findAll().stream()
            .map(this::convertToOrderItemDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderItemDto> getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
            .map(this::convertToOrderItemDto);
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
}
