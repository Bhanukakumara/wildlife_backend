package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.ProductCategory.ProductCategoryCreateDto;
import com.example.wildlife_backend.dto.ProductCategory.ProductCategoryGetDto;
import com.example.wildlife_backend.entity.Product;
import com.example.wildlife_backend.entity.ProductCategory;
import com.example.wildlife_backend.entity.Promotion;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.ProductCategoryRepository;
import com.example.wildlife_backend.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategoryGetDto createProductCategory(ProductCategoryCreateDto productCategoryCreateDto) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(productCategoryCreateDto.getName());
        productCategory.setDescription(productCategoryCreateDto.getDescription());
        productCategory.setImageUrl(productCategoryCreateDto.getImageUrl());
        productCategory.setActive(productCategoryCreateDto.isActive());
        productCategory.setCreatedBy(productCategoryCreateDto.getCreatedBy());
        productCategory.setUpdatedBy(productCategoryCreateDto.getUpdatedBy());

        ProductCategory savedCategory = productCategoryRepository.save(productCategory);
        return convertToGetDto(savedCategory);
    }

    @Override
    public Optional<ProductCategoryGetDto> getProductCategoryById(Long productCategoryId) {
        return productCategoryRepository.findById(productCategoryId)
                .map(this::convertToGetDto);
    }

    @Override
    public List<ProductCategoryGetDto> getAllProductCategories() {
        return productCategoryRepository.findAll()
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductCategoryGetDto> updateProductCategory(Long productCategoryId, ProductCategoryCreateDto productCategoryCreateDto) {
        ProductCategory existingCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductCategory not found with id: " + productCategoryId));

        existingCategory.setName(productCategoryCreateDto.getName());
        existingCategory.setDescription(productCategoryCreateDto.getDescription());
        existingCategory.setImageUrl(productCategoryCreateDto.getImageUrl());
        existingCategory.setActive(productCategoryCreateDto.isActive());
        existingCategory.setUpdatedBy(productCategoryCreateDto.getUpdatedBy());

        ProductCategory updatedCategory = productCategoryRepository.save(existingCategory);
        return Optional.of(convertToGetDto(updatedCategory));
    }

    @Override
    public boolean deleteProductCategory(Long productCategoryId) {
        if (productCategoryRepository.existsById(productCategoryId)) {
            productCategoryRepository.deleteById(productCategoryId);
        }
        return !productCategoryRepository.existsById(productCategoryId);
    }

    private ProductCategoryGetDto convertToGetDto(ProductCategory productCategory) {
        Set<Long> productIds = productCategory.getProducts() != null ?
                productCategory.getProducts().stream()
                        .map(Product::getId)
                        .collect(Collectors.toSet()) :
                new HashSet<>();

        Set<Long> promotionIds = productCategory.getPromotions() != null ?
                productCategory.getPromotions().stream()
                        .map(Promotion::getId)
                        .collect(Collectors.toSet()) :
                new HashSet<>();

        return ProductCategoryGetDto.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .description(productCategory.getDescription())
                .imageUrl(productCategory.getImageUrl())
                .active(productCategory.isActive())
                .createdAt(productCategory.getCreatedAt())
                .updatedAt(productCategory.getUpdatedAt())
                .createdBy(productCategory.getCreatedBy())
                .updatedBy(productCategory.getUpdatedBy())
                .productIds(productIds)
                .promotionIds(promotionIds)
                .build();
    }
}
