package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.ShopOrder.ShopOrderCreateDto;
import com.example.wildlife_backend.dto.ShopOrder.ShopOrderGetDto;

import java.util.List;
import java.util.Optional;

public interface ShopOrderService {
    ShopOrderGetDto createShopOrder(ShopOrderCreateDto shopOrderCreateDto);
    Optional<ShopOrderGetDto> getShopOrderById(Long shopOrderId);
    List<ShopOrderGetDto> getAllShopOrders();
    Optional<ShopOrderGetDto> updateShopOrder(Long shopOrderId, ShopOrderCreateDto shopOrderCreateDto);
    void deleteShopOrder(Long shopOrderId);
}