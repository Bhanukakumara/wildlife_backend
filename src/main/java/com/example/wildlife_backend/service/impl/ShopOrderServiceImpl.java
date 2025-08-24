package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.ShopOrder.ShopOrderCreateDto;
import com.example.wildlife_backend.dto.ShopOrder.ShopOrderGetDto;
import com.example.wildlife_backend.entity.Address;
import com.example.wildlife_backend.entity.ShippingMethod;
import com.example.wildlife_backend.entity.ShopOrder;
import com.example.wildlife_backend.entity.UserPaymentMethod;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.AddressRepository;
import com.example.wildlife_backend.repository.ShippingMethodRepository;
import com.example.wildlife_backend.repository.ShopOrderRepository;
import com.example.wildlife_backend.repository.UserPaymentMethodRepository;
import com.example.wildlife_backend.service.ShopOrderService;
import com.example.wildlife_backend.util.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopOrderServiceImpl implements ShopOrderService {
    private final ShopOrderRepository shopOrderRepository;
    private final UserPaymentMethodRepository userPaymentMethodRepository;
    private final AddressRepository addressRepository;
    private final ShippingMethodRepository shippingMethodRepository;

    @Override
    public ShopOrderGetDto createShopOrder(ShopOrderCreateDto shopOrderCreateDto) {
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setOrderDate(shopOrderCreateDto.getOrderDate());
        shopOrder.setOrderTotal(shopOrderCreateDto.getOrderTotal());
        shopOrder.setOrderStatus(shopOrderCreateDto.getOrderStatus());
        
        // Set user payment method
        if (shopOrderCreateDto.getUserPaymentMethodId() != null) {
            UserPaymentMethod userPaymentMethod = userPaymentMethodRepository.findById(shopOrderCreateDto.getUserPaymentMethodId())
                    .orElseThrow(() -> new ResourceNotFoundException("UserPaymentMethod not found with id: " + shopOrderCreateDto.getUserPaymentMethodId()));
            shopOrder.setUserPaymentMethod(userPaymentMethod);
        }
        
        // Set shipping address
        if (shopOrderCreateDto.getShippingAddressId() != null) {
            Address address = addressRepository.findById(shopOrderCreateDto.getShippingAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + shopOrderCreateDto.getShippingAddressId()));
            shopOrder.setAddress(address);
        }
        
        // Set shipping method
        if (shopOrderCreateDto.getShippingMethodId() != null) {
            ShippingMethod shippingMethod = shippingMethodRepository.findById(shopOrderCreateDto.getShippingMethodId())
                    .orElseThrow(() -> new ResourceNotFoundException("ShippingMethod not found with id: " + shopOrderCreateDto.getShippingMethodId()));
            shopOrder.setShippingMethod(shippingMethod);
        }
        
        ShopOrder savedShopOrder = shopOrderRepository.save(shopOrder);
        return convertToGetDto(savedShopOrder);
    }

    @Override
    public Optional<ShopOrderGetDto> getShopOrderById(Long shopOrderId) {
        return shopOrderRepository.findById(shopOrderId)
                .map(this::convertToGetDto);
    }

    @Override
    public List<ShopOrderGetDto> getAllShopOrders() {
        return shopOrderRepository.findAll()
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ShopOrderGetDto> updateShopOrder(Long shopOrderId, ShopOrderCreateDto shopOrderCreateDto) {
        ShopOrder existingShopOrder = shopOrderRepository.findById(shopOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("ShopOrder not found with id: " + shopOrderId));
        
        existingShopOrder.setOrderDate(shopOrderCreateDto.getOrderDate());
        existingShopOrder.setOrderTotal(shopOrderCreateDto.getOrderTotal());
        existingShopOrder.setOrderStatus(shopOrderCreateDto.getOrderStatus());
        
        // Update user payment method if provided
        if (shopOrderCreateDto.getUserPaymentMethodId() != null) {
            UserPaymentMethod userPaymentMethod = userPaymentMethodRepository.findById(shopOrderCreateDto.getUserPaymentMethodId())
                    .orElseThrow(() -> new ResourceNotFoundException("UserPaymentMethod not found with id: " + shopOrderCreateDto.getUserPaymentMethodId()));
            existingShopOrder.setUserPaymentMethod(userPaymentMethod);
        }
        
        // Update shipping address if provided
        if (shopOrderCreateDto.getShippingAddressId() != null) {
            Address address = addressRepository.findById(shopOrderCreateDto.getShippingAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + shopOrderCreateDto.getShippingAddressId()));
            existingShopOrder.setAddress(address);
        }
        
        // Update shipping method if provided
        if (shopOrderCreateDto.getShippingMethodId() != null) {
            ShippingMethod shippingMethod = shippingMethodRepository.findById(shopOrderCreateDto.getShippingMethodId())
                    .orElseThrow(() -> new ResourceNotFoundException("ShippingMethod not found with id: " + shopOrderCreateDto.getShippingMethodId()));
            existingShopOrder.setShippingMethod(shippingMethod);
        }
        
        ShopOrder updatedShopOrder = shopOrderRepository.save(existingShopOrder);
        return Optional.of(convertToGetDto(updatedShopOrder));
    }

    @Override
    public boolean deleteShopOrder(Long shopOrderId) {
        if (shopOrderRepository.findById(shopOrderId).isPresent()) {
            shopOrderRepository.deleteById(shopOrderId);
        }
        return true;
    }

    @Override
    public List<ShopOrderGetDto> getShopOrdersByPaymentMethodId(Long paymentMethodId) {
        return List.of();
    }

    @Override
    public List<ShopOrderGetDto> getShopOrdersByAddressId(Long addressId) {
        return List.of();
    }

    @Override
    public List<ShopOrderGetDto> getShopOrdersByShippingMethodId(Long shippingMethodId) {
        return List.of();
    }

    @Override
    public List<ShopOrderGetDto> getShopOrdersByStatus(OrderStatus status) {
        return List.of();
    }

    @Override
    public List<ShopOrderGetDto> getShopOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return List.of();
    }

    @Override
    public List<ShopOrderGetDto> bulkCreateShopOrders(List<ShopOrderCreateDto> shopOrderCreateDtos) {
        return List.of();
    }

    @Override
    public boolean validateShopOrder(ShopOrderCreateDto shopOrderCreateDto) {
        return false;
    }

    @Override
    public List<ShopOrderGetDto> getShopOrdersWithOrderLines() {
        return List.of();
    }

    private ShopOrderGetDto convertToGetDto(ShopOrder shopOrder) {
        return ShopOrderGetDto.builder()
                .id(shopOrder.getId())
                .orderDate(shopOrder.getOrderDate())
                .userPaymentMethodId(shopOrder.getUserPaymentMethod() != null ? shopOrder.getUserPaymentMethod().getId() : null)
                .shippingAddressId(shopOrder.getAddress() != null ? shopOrder.getAddress().getId() : null)
                .shippingMethodId(shopOrder.getShippingMethod() != null ? shopOrder.getShippingMethod().getId() : null)
                .orderTotal(shopOrder.getOrderTotal())
                .orderStatus(shopOrder.getOrderStatus())
                .build();
    }
}