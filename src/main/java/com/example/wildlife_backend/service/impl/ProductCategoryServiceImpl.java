package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.ProductCategoryCreateDto;
import com.example.wildlife_backend.dto.ProductCategoryGetDto;
import com.example.wildlife_backend.entity.ProductCategory;
import com.example.wildlife_backend.exception.DuplicateResourceException;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.ProductCategoryRepository;
import com.example.wildlife_backend.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    @Transactional
    public ProductCategoryGetDto createCategory(ProductCategoryCreateDto categoryCreateDto) {
        validateCategoryCreation(categoryCreateDto);

        ProductCategory category = convertCreateDtoToCategory(categoryCreateDto);
        ProductCategory savedCategory = productCategoryRepository.save(category);

        log.info("Created new product category with ID: {}", savedCategory.getId());
        return convertCategoryToGetDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCategoryGetDto> getCategoryById(Long categoryId) {
        return productCategoryRepository.findById(categoryId)
                .map(this::convertCategoryToGetDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCategoryGetDto> getCategoryByName(String name) {
        return productCategoryRepository.findByName(name)
                .map(this::convertCategoryToGetDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryGetDto> getAllCategories() {
        List<ProductCategory> categories = productCategoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("No categories found");
        }
        return categories.stream()
                .map(this::convertCategoryToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryGetDto> getAllActiveCategories() {
        List<ProductCategory> activeCategories = productCategoryRepository.findByActive(true);
        if (activeCategories.isEmpty()) {
            throw new ResourceNotFoundException("No active categories found");
        }
        return activeCategories.stream()
                .map(this::convertCategoryToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean updateCategory(Long categoryId, ProductCategoryCreateDto categoryDetails) {
        ProductCategory existingCategory = productCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        validateCategoryUpdate(existingCategory, categoryDetails);
        updateCategoryFromDto(existingCategory, categoryDetails);
        productCategoryRepository.save(existingCategory);
        
        log.info("Updated category with ID: {}", categoryId);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long categoryId) {
        ProductCategory category = productCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        
        productCategoryRepository.delete(category);
        log.info("Deleted category with ID: {}", categoryId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryGetDto> searchCategoriesByKeyword(String keyword) {
        List<ProductCategory> categories = productCategoryRepository.searchByKeyword(keyword);
        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("No categories found with keyword: " + keyword);
        }
        return categories.stream()
                .map(this::convertCategoryToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return productCategoryRepository.existsByName(name);
    }

    private void validateCategoryCreation(ProductCategoryCreateDto categoryCreateDto) {
        if (productCategoryRepository.existsByName(categoryCreateDto.getName())) {
            throw new DuplicateResourceException("Category name already exists: " + categoryCreateDto.getName());
        }
    }

    private void validateCategoryUpdate(ProductCategory existingCategory, ProductCategoryCreateDto categoryDetails) {
        if (!existingCategory.getName().equals(categoryDetails.getName()) &&
                productCategoryRepository.existsByName(categoryDetails.getName())) {
            throw new DuplicateResourceException("Category name already exists: " + categoryDetails.getName());
        }
    }

    private ProductCategory convertCreateDtoToCategory(ProductCategoryCreateDto dto) {
        ProductCategory category = new ProductCategory();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setImageUrl(dto.getImageUrl());
        category.setActive(dto.isActive());
        return category;
    }

    private ProductCategoryGetDto convertCategoryToGetDto(ProductCategory category) {
        return ProductCategoryGetDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .active(category.isActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .createdBy(category.getCreatedBy())
                .updatedBy(category.getUpdatedBy())
                .build();
    }

    private void updateCategoryFromDto(ProductCategory category, ProductCategoryCreateDto dto) {
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setImageUrl(dto.getImageUrl());
        category.setActive(dto.isActive());
    }
}
