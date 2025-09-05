package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.ProductItem.ProductItemCreateDto;
import com.example.wildlife_backend.dto.ProductItem.ProductItemGetDto;
import com.example.wildlife_backend.entity.Product;
import com.example.wildlife_backend.entity.ProductItem;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.ProductItemRepository;
import com.example.wildlife_backend.repository.ProductRepository;
import com.example.wildlife_backend.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductItemServiceImpl implements ProductItemService {
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductItemGetDto createProductItem(ProductItemCreateDto productItemCreateDto) {
        ProductItem productItem = new ProductItem();
        productItem.setName(productItemCreateDto.getName());
        productItem.setSku(productItemCreateDto.getSku());
        productItem.setDescription(productItemCreateDto.getDescription());
        productItem.setPrice(productItemCreateDto.getPrice());
        productItem.setWeight(productItemCreateDto.getWeight());
        productItem.setWeightUnit(productItemCreateDto.getWeightUnit());
        productItem.setLength(productItemCreateDto.getLength());
        productItem.setWidth(productItemCreateDto.getWidth());
        productItem.setHeight(productItemCreateDto.getHeight());
        productItem.setCustomizable(productItemCreateDto.isCustomizable());
        productItem.setFreeShipping(productItemCreateDto.isFreeShipping());
        productItem.setQtyInStock(productItemCreateDto.getQtyInStock());
        productItem.setImageUrl(productItemCreateDto.getImageUrl());

        Product product = productRepository.findById(productItemCreateDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productItemCreateDto.getProductId()));
        productItem.setProduct(product);

        ProductItem savedProductItem = productItemRepository.save(productItem);
        return convertToGetDto(savedProductItem);
    }

    @Override
    public Optional<ProductItemGetDto> getProductItemById(Long productItemId) {
        return productItemRepository.findById(productItemId)
                .map(this::convertToGetDto);
    }

    @Override
    public Optional<ProductItemGetDto> getProductItemBySku(String sku) {
        return productItemRepository.findBySku(sku)
                .map(this::convertToGetDto);
    }

    @Override
    public List<ProductItemGetDto> getAvailableProductItems() {
        return productItemRepository.findByQtyInStockGreaterThan(0)
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductItemGetDto> getAvailableProductItemsByProduct(Long productId) {
        return productItemRepository.findByProductIdAndQtyInStockGreaterThan(productId, 0)
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductItemGetDto> getAllProductItems() {
        return productItemRepository.findAll()
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductItemGetDto> getProductItemsByProduct(Long productId) {
        return productItemRepository.findByProductId(productId)
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductItemGetDto> searchProductItemsByKeyword(String keyword) {
        return productItemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductItemGetDto> updateProductItem(Long productItemId, ProductItemCreateDto productItemCreateDto) {
        ProductItem existingProductItem = productItemRepository.findById(productItemId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductItem not found with id: " + productItemId));

        existingProductItem.setName(productItemCreateDto.getName());
        existingProductItem.setSku(productItemCreateDto.getSku());
        existingProductItem.setDescription(productItemCreateDto.getDescription());
        existingProductItem.setPrice(productItemCreateDto.getPrice());
        existingProductItem.setWeight(productItemCreateDto.getWeight());
        existingProductItem.setWeightUnit(productItemCreateDto.getWeightUnit());
        existingProductItem.setLength(productItemCreateDto.getLength());
        existingProductItem.setWidth(productItemCreateDto.getWidth());
        existingProductItem.setHeight(productItemCreateDto.getHeight());
        existingProductItem.setCustomizable(productItemCreateDto.isCustomizable());
        existingProductItem.setFreeShipping(productItemCreateDto.isFreeShipping());
        existingProductItem.setQtyInStock(productItemCreateDto.getQtyInStock());
        existingProductItem.setImageUrl(productItemCreateDto.getImageUrl());

        Product product = productRepository.findById(productItemCreateDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productItemCreateDto.getProductId()));
        existingProductItem.setProduct(product);

        ProductItem updatedProductItem = productItemRepository.save(existingProductItem);
        return Optional.of(convertToGetDto(updatedProductItem));
    }

    @Override
    public boolean deleteProductItem(Long productItemId) {
        if (productItemRepository.existsById(productItemId)) {
            productItemRepository.deleteById(productItemId);
        }
        return !productItemRepository.existsById(productItemId);
    }

    private ProductItemGetDto convertToGetDto(ProductItem productItem) {
        return ProductItemGetDto.builder()
                .id(productItem.getId())
                .name(productItem.getName())
                .sku(productItem.getSku())
                .description(productItem.getDescription())
                .price(productItem.getPrice())
                .weight(productItem.getWeight())
                .weightUnit(productItem.getWeightUnit())
                .length(productItem.getLength())
                .width(productItem.getWidth())
                .height(productItem.getHeight())
                .customizable(productItem.isCustomizable())
                .freeShipping(productItem.isFreeShipping())
                .qtyInStock(productItem.getQtyInStock())
                .imageUrl(productItem.getImageUrl())
                .createdAt(productItem.getCreatedAt())
                .updatedAt(productItem.getUpdatedAt())
                .productId(productItem.getProduct().getId())
                .build();
    }
}
