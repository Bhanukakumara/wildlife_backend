package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.category.CategoryCreateDto;
import com.example.wildlife_backend.dto.category.CategoryResponseDto;
import com.example.wildlife_backend.dto.category.CategoryUpdateDto;
import com.example.wildlife_backend.entity.Category;
import com.example.wildlife_backend.repository.CategoryRepository;
import com.example.wildlife_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDto createCategory(CategoryCreateDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setSlug(categoryDto.getSlug());
        category.setDescription(categoryDto.getDescription());
        category.setImageUrl(categoryDto.getImageUrl());
        category.setIconUrl(categoryDto.getIconUrl());
        category.setDisplayOrder(categoryDto.getDisplayOrder());
        category.setIsFeatured(categoryDto.getIsFeatured());

        Category savedCategory = createCategoryEntity(category);
        return convertToCategoryResponseDto(savedCategory);
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryUpdateDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        // Update allowed fields
        if (categoryDto.getName() != null && !categoryDto.getName().equals(existingCategory.getName())) {
            if (!isNameAvailable(categoryDto.getName())) {
                throw new RuntimeException("Category name already exists");
            }
            existingCategory.setName(categoryDto.getName());
            existingCategory.setSlug(generateSlug(categoryDto.getName()));
        }

        if (categoryDto.getSlug() != null && !categoryDto.getSlug().equals(existingCategory.getSlug())) {
            if (!isSlugAvailable(categoryDto.getSlug())) {
                throw new RuntimeException("Category slug already exists");
            }
            existingCategory.setSlug(categoryDto.getSlug());
        }

        if (categoryDto.getDescription() != null) {
            existingCategory.setDescription(categoryDto.getDescription());
        }

        if (categoryDto.getImageUrl() != null) {
            existingCategory.setImageUrl(categoryDto.getImageUrl());
        }

        if (categoryDto.getIconUrl() != null) {
            existingCategory.setIconUrl(categoryDto.getIconUrl());
        }

        if (categoryDto.getDisplayOrder() != null) {
            existingCategory.setDisplayOrder(categoryDto.getDisplayOrder());
        }

        if (categoryDto.getIsFeatured() != null) {
            existingCategory.setIsFeatured(categoryDto.getIsFeatured());
        }

        Category savedCategory = categoryRepository.save(existingCategory);
        return convertToCategoryResponseDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryResponseDto> getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .map(this::convertToCategoryResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryResponseDto> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
            .map(this::convertToCategoryResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(this::convertToCategoryResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        // Check if category has subcategories
        if (!category.getChildren().isEmpty()) {
            throw new RuntimeException("Cannot delete category with subcategories");
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDto createSubcategory(Long parentId, CategoryCreateDto subcategoryDto) {
        Category parent = categoryRepository.findById(parentId)
            .orElseThrow(() -> new RuntimeException("Parent category not found"));

        Category subcategory = new Category();
        subcategory.setName(subcategoryDto.getName());
        subcategory.setSlug(subcategoryDto.getSlug());
        subcategory.setDescription(subcategoryDto.getDescription());
        subcategory.setImageUrl(subcategoryDto.getImageUrl());
        subcategory.setIconUrl(subcategoryDto.getIconUrl());
        subcategory.setDisplayOrder(subcategoryDto.getDisplayOrder());
        subcategory.setIsFeatured(subcategoryDto.getIsFeatured());
        subcategory.setParent(parent);

        Category savedSubcategory = createCategoryEntity(subcategory);
        return convertToCategoryResponseDto(savedSubcategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getRootCategories() {
        return categoryRepository.findRootCategories().stream()
            .map(this::convertToCategoryResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getSubcategories(Long parentId) {
        return categoryRepository.findSubcategoriesByParentId(parentId).stream()
            .map(this::convertToCategoryResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getActiveCategories() {
        return categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
            .map(this::convertToCategoryResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getFeaturedCategories() {
        return categoryRepository.findByIsFeaturedTrue().stream()
            .map(this::convertToCategoryResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> searchCategories(String keyword) {
        return categoryRepository.searchCategoriesByName(keyword).stream()
            .map(this::convertToCategoryResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto activateCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setIsActive(true);
        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryResponseDto(savedCategory);
    }

    @Override
    public CategoryResponseDto deactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setIsActive(false);
        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryResponseDto(savedCategory);
    }

    @Override
    public CategoryResponseDto updateDisplayOrder(Long id, Integer displayOrder) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setDisplayOrder(displayOrder);
        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryResponseDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public long getPhotoCountInCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        return categoryRepository.countPhotosInCategory(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategoriesWithPhotoCount() {
        return categoryRepository.findByIsActiveTrue().stream()
            .map(this::convertToCategoryResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNameAvailable(String name) {
        return !categoryRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSlugAvailable(String slug) {
        return !categoryRepository.existsBySlug(slug);
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
            .replaceAll("[^a-z0-9\\s]", "")
            .replaceAll("\\s+", "-")
            .replaceAll("-+", "-")
            .replaceAll("^-|-", "");
    }

    private Category createCategoryEntity(Category category) {
        if (!isNameAvailable(category.getName())) {
            throw new RuntimeException("Category name already exists");
        }
        if (!isSlugAvailable(category.getSlug())) {
            throw new RuntimeException("Category slug already exists");
        }

        category.setIsActive(true);
        if (category.getDisplayOrder() == null) {
            category.setDisplayOrder(0);
        }
        if (category.getPhotoCount() == null) {
            category.setPhotoCount(0);
        }

        return categoryRepository.save(category);
    }

    private CategoryResponseDto convertToCategoryResponseDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setImageUrl(category.getImageUrl());
        dto.setIconUrl(category.getIconUrl());
        dto.setDisplayOrder(category.getDisplayOrder());
        dto.setIsFeatured(category.getIsFeatured());
        dto.setIsActive(category.getIsActive());
        dto.setPhotoCount(category.getPhotoCount());
        dto.setParentId(category.getParent() != null ? category.getParent().getId() : null);
        dto.setParentName(category.getParent() != null ? category.getParent().getName() : null);
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
}
