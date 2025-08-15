package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.ProductItemCreateDto;
import com.example.wildlife_backend.dto.ProductItemGetDto;
import com.example.wildlife_backend.entity.ProductItem;
import com.example.wildlife_backend.entity.Product;
import com.example.wildlife_backend.exception.DuplicateResourceException;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.ProductItemRepository;
import com.example.wildlife_backend.repository.ProductRepository;
import com.example.wildlife_backend.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductItemServiceImpl implements ProductItemService {

    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductItemGetDto createProductItem(ProductItemCreateDto productItemCreateDto) {
        validateProductItemCreation(productItemCreateDto);

        ProductItem productItem = convertCreateDtoToProductItem(productItemCreateDto);
        ProductItem savedProductItem = productItemRepository.save(productItem);

        log.info("Created new product item with ID: {}", savedProductItem.getId());
        return convertProductItemToGetDto(savedProductItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductItemGetDto> getProductItemById(Long productItemId) {
        return productItemRepository.findById(productItemId)
                .map(this::convertProductItemToGetDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductItemGetDto> getProductItemBySku(String sku) {
        return productItemRepository.findBySku(sku)
                .map(this::convertProductItemToGetDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductItemGetDto> getAllProductItems() {
        List<ProductItem> productItems = productItemRepository.findAll();
        if (productItems.isEmpty()) {
            throw new ResourceNotFoundException("No product items found");
        }
        return productItems.stream()
                .map(this::convertProductItemToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductItemGetDto> getProductItemsByProduct(Long productId) {
        List<ProductItem> productItems = productItemRepository.findByProductId(productId);
        if (productItems.isEmpty()) {
            throw new ResourceNotFoundException("No product items found for product ID: " + productId);
        }
        return productItems.stream()
                .map(this::convertProductItemToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean updateProductItem(Long productItemId, ProductItemCreateDto productItemDetails) {
        ProductItem existingProductItem = productItemRepository.findById(productItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Product item not found with id: " + productItemId));

        validateProductItemUpdate(existingProductItem, productItemDetails);
        updateProductItemFromDto(existingProductItem, productItemDetails);
        productItemRepository.save(existingProductItem);
        
        log.info("Updated product item with ID: {}", productItemId);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteProductItem(Long productItemId) {
        ProductItem productItem = productItemRepository.findById(productItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Product item not found with id: " + productItemId));
        
        productItemRepository.delete(productItem);
        log.info("Deleted product item with ID: {}", productItemId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductItemGetDto> getAvailableProductItems() {
        List<ProductItem> availableItems = productItemRepository.findAvailableItems();
        if (availableItems.isEmpty()) {
            throw new ResourceNotFoundException("No available product items found");
        }
        return availableItems.stream()
                .map(this::convertProductItemToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductItemGetDto> getAvailableProductItemsByProduct(Long productId) {
        List<ProductItem> availableItems = productItemRepository.findAvailableItemsByProduct(productId);
        if (availableItems.isEmpty()) {
            throw new ResourceNotFoundException("No available product items found for product ID: " + productId);
        }
        return availableItems.stream()
                .map(this::convertProductItemToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductItemGetDto> searchProductItemsByKeyword(String keyword) {
        List<ProductItem> productItems = productItemRepository.searchByKeyword(keyword);
        if (productItems.isEmpty()) {
            throw new ResourceNotFoundException("No product items found with keyword: " + keyword);
        }
        return productItems.stream()
                .map(this::convertProductItemToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductItemGetDto> getProductItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<ProductItem> productItems = productItemRepository.findByPriceRange(minPrice, maxPrice);
        if (productItems.isEmpty()) {
            throw new ResourceNotFoundException("No product items found in price range: " + minPrice + " - " + maxPrice);
        }
        return productItems.stream()
                .map(this::convertProductItemToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return productItemRepository.existsBySku(sku);
    }

    private void validateProductItemCreation(ProductItemCreateDto productItemCreateDto) {
        if (productItemRepository.existsBySku(productItemCreateDto.getSku())) {
            throw new DuplicateResourceException("SKU already exists: " + productItemCreateDto.getSku());
        }
        
        if (productItemCreateDto.getProductId() != null) {
            productRepository.findById(productItemCreateDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productItemCreateDto.getProductId()));
        }
    }

    private void validateProductItemUpdate(ProductItem existingProductItem, ProductItemCreateDto productItemDetails) {
        if (!existingProductItem.getSku().equals(productItemDetails.getSku()) &&
                productItemRepository.existsBySku(productItemDetails.getSku())) {
            throw new DuplicateResourceException("SKU already exists: " + productItemDetails.getSku());
        }
        
        if (productItemDetails.getProductId() != null) {
            productRepository.findById(productItemDetails.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productItemDetails.getProductId()));
        }
    }

    private ProductItem convertCreateDtoToProductItem(ProductItemCreateDto dto) {
        ProductItem productItem = new ProductItem();
        productItem.setName(dto.getName());
        productItem.setSku(dto.getSku());
        productItem.setDescription(dto.getDescription());
        productItem.setPrice(dto.getPrice());
        productItem.setWeight(dto.getWeight());
        productItem.setWeightUnit(dto.getWeightUnit());
        productItem.setLength(dto.getLength());
        productItem.setWidth(dto.getWidth());
        productItem.setHeight(dto.getHeight());
        productItem.setCustomizable(dto.isCustomizable());
        productItem.setFreeShipping(dto.isFreeShipping());
        productItem.setQtyInStock(dto.getQtyInStock());
        productItem.setImageUrl(dto.getImageUrl());
        
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + dto.getProductId()));
            productItem.setProduct(product);
        }
        
        return productItem;
    }

    private ProductItemGetDto convertProductItemToGetDto(ProductItem productItem) {
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

    private void updateProductItemFromDto(ProductItem productItem, ProductItemCreateDto dto) {
        productItem.setName(dto.getName());
        productItem.setSku(dto.getSku());
        productItem.setDescription(dto.getDescription());
        productItem.setPrice(dto.getPrice());
        productItem.setWeight(dto.getWeight());
        productItem.setWeightUnit(dto.getWeightUnit());
        productItem.setLength(dto.getLength());
        productItem.setWidth(dto.getWidth());
        productItem.setHeight(dto.getHeight());
        productItem.setCustomizable(dto.isCustomizable());
        productItem.setFreeShipping(dto.isFreeShipping());
        productItem.setQtyInStock(dto.getQtyInStock());
        productItem.setImageUrl(dto.getImageUrl());
        
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + dto.getProductId()));
            productItem.setProduct(product);
        }
    }
}
