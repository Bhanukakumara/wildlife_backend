package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.OrderLine.OrderLineCreateDto;
import com.example.wildlife_backend.dto.OrderLine.OrderLineGetDto;
import com.example.wildlife_backend.entity.OrderLine;
import com.example.wildlife_backend.entity.ProductItem;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.OrderLineRepository;
import com.example.wildlife_backend.repository.ProductItemRepository;
import com.example.wildlife_backend.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {
    private final OrderLineRepository orderLineRepository;
    private final ProductItemRepository productItemRepository;

    @Override
    public OrderLineGetDto createOrderLine(OrderLineCreateDto orderLineCreateDto) {
        OrderLine orderLine = new OrderLine();
        
        // Set product item
        if (orderLineCreateDto.getProductItemId() != null) {
            ProductItem productItem = productItemRepository.findById(orderLineCreateDto.getProductItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductItem not found with id: " + orderLineCreateDto.getProductItemId()));
            orderLine.setProductItem(productItem);
        }
        
        orderLine.setQuantity(orderLineCreateDto.getQuantity());
        orderLine.setPrice(orderLineCreateDto.getPrice());
        
        OrderLine savedOrderLine = orderLineRepository.save(orderLine);
        return convertToGetDto(savedOrderLine);
    }

    @Override
    public Optional<OrderLineGetDto> getOrderLineById(Long orderLineId) {
        return orderLineRepository.findById(orderLineId)
                .map(this::convertToGetDto);
    }

    @Override
    public List<OrderLineGetDto> getAllOrderLines() {
        return orderLineRepository.findAll()
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderLineGetDto> updateOrderLine(Long orderLineId, OrderLineCreateDto orderLineCreateDto) {
        OrderLine existingOrderLine = orderLineRepository.findById(orderLineId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderLine not found with id: " + orderLineId));
        
        // Update product item if provided
        if (orderLineCreateDto.getProductItemId() != null) {
            ProductItem productItem = productItemRepository.findById(orderLineCreateDto.getProductItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductItem not found with id: " + orderLineCreateDto.getProductItemId()));
            existingOrderLine.setProductItem(productItem);
        }
        
        existingOrderLine.setQuantity(orderLineCreateDto.getQuantity());
        existingOrderLine.setPrice(orderLineCreateDto.getPrice());
        
        OrderLine updatedOrderLine = orderLineRepository.save(existingOrderLine);
        return Optional.of(convertToGetDto(updatedOrderLine));
    }

    @Override
    public boolean deleteOrderLine(Long orderLineId) {
        if (!orderLineRepository.existsById(orderLineId)) {
            throw new ResourceNotFoundException("OrderLine not found with id: " + orderLineId);
        } else {
            orderLineRepository.deleteById(orderLineId);
            return true;
        }
    }

    @Override
    public List<OrderLineGetDto> getOrderLinesByOrderId(Long orderId) {
        return orderLineRepository.findByShopOrderId(orderId)
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderLineGetDto> getOrderLinesByProductItemId(Long productItemId) {
        return orderLineRepository.findByProductItemId(productItemId)
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderLineGetDto> bulkCreateOrderLines(List<OrderLineCreateDto> orderLineCreateDtos) {
        List<OrderLine> orderLines = orderLineCreateDtos.stream()
                .map(dto -> {
                    OrderLine orderLine = new OrderLine();
                    if (dto.getProductItemId() != null) {
                        ProductItem productItem = productItemRepository.findById(dto.getProductItemId())
                                .orElseThrow(() -> new ResourceNotFoundException("ProductItem not found with id: " + dto.getProductItemId()));
                        orderLine.setProductItem(productItem);
                    }
                    orderLine.setQuantity(dto.getQuantity());
                    orderLine.setPrice(dto.getPrice());
                    return orderLine;
                })
                .collect(Collectors.toList());
        List<OrderLine> savedOrderLines = orderLineRepository.saveAll(orderLines);
        return savedOrderLines.stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

@Override
public boolean validateOrderLine(OrderLineCreateDto orderLineCreateDto) {
    return orderLineCreateDto.getQuantity() > 0 && orderLineCreateDto.getPrice().compareTo(BigDecimal.ZERO) > 0;
}

    @Override
    public List<OrderLineGetDto> getOrderLinesWithReviews() {
        // Assuming we have a Review entity and a relationship between OrderLine and Review
        // For now, we return an empty list
        return List.of();
    }

    private OrderLineGetDto convertToGetDto(OrderLine orderLine) {
        return OrderLineGetDto.builder()
                .id(orderLine.getId())
                .productItemId(orderLine.getProductItem() != null ? orderLine.getProductItem().getId() : null)
                .orderId(orderLine.getShopOrder() != null ? orderLine.getShopOrder().getId() : null)
                .quantity(orderLine.getQuantity())
                .price(orderLine.getPrice())
                .build();
    }
}
