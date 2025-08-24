package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.ShopOrder.ShopOrderCreateDto;
import com.example.wildlife_backend.dto.ShopOrder.ShopOrderGetDto;
import com.example.wildlife_backend.util.OrderStatus;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShopOrderService {
    ShopOrderGetDto createShopOrder(ShopOrderCreateDto shopOrderCreateDto);
    Optional<ShopOrderGetDto> getShopOrderById(Long shopOrderId);
    List<ShopOrderGetDto> getAllShopOrders();
    Optional<ShopOrderGetDto> updateShopOrder(Long shopOrderId, ShopOrderCreateDto shopOrderCreateDto);
    boolean deleteShopOrder(Long shopOrderId);

    List<ShopOrderGetDto> getShopOrdersByPaymentMethodId(Long paymentMethodId);

    List<ShopOrderGetDto> getShopOrdersByAddressId(Long addressId);

    List<ShopOrderGetDto> getShopOrdersByShippingMethodId(Long shippingMethodId);

    List<ShopOrderGetDto> getShopOrdersByStatus(OrderStatus status);

    List<ShopOrderGetDto> getShopOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<ShopOrderGetDto> bulkCreateShopOrders(@Valid List<ShopOrderCreateDto> shopOrderCreateDtos);

    boolean validateShopOrder(@Valid ShopOrderCreateDto shopOrderCreateDto);

    List<ShopOrderGetDto> getShopOrdersWithOrderLines();
}