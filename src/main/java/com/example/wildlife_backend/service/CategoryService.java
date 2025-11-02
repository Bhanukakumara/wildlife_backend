package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.category.CategoryCreateDto;
import com.example.wildlife_backend.dto.category.CategoryResponseDto;
import com.example.wildlife_backend.dto.category.CategoryUpdateDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    // CRUD operations
    CategoryResponseDto createCategory(CategoryCreateDto categoryDto);

    CategoryResponseDto updateCategory(Long id, CategoryUpdateDto categoryDto);

    Optional<CategoryResponseDto> getCategoryById(Long id);

    Optional<CategoryResponseDto> getCategoryBySlug(String slug);

    List<CategoryResponseDto> getAllCategories();

    void deleteCategory(Long id);

    // Business logic methods
    CategoryResponseDto createSubcategory(Long parentId, CategoryCreateDto subcategoryDto);

    List<CategoryResponseDto> getRootCategories();

    List<CategoryResponseDto> getSubcategories(Long parentId);

    List<CategoryResponseDto> getActiveCategories();

    List<CategoryResponseDto> getFeaturedCategories();

    List<CategoryResponseDto> searchCategories(String keyword);

    CategoryResponseDto activateCategory(Long id);

    CategoryResponseDto deactivateCategory(Long id);

    CategoryResponseDto updateDisplayOrder(Long id, Integer displayOrder);

    // Statistics methods
    long getPhotoCountInCategory(Long categoryId);

    List<CategoryResponseDto> getCategoriesWithPhotoCount();

    // Validation methods
    boolean isNameAvailable(String name);

    boolean isSlugAvailable(String slug);
}
