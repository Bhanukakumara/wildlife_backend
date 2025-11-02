package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    Optional<Category> findBySlug(String slug);

    List<Category> findByParent(Category parent);

    List<Category> findByParentIsNull();

    List<Category> findByIsActiveTrue();

    List<Category> findByIsFeaturedTrue();

    List<Category> findByIsActiveTrueOrderByDisplayOrderAsc();

    @Query("SELECT c FROM Category c WHERE c.isActive = true AND LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Category> searchCategoriesByName(@Param("keyword") String keyword);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.isActive = true ORDER BY c.displayOrder ASC")
    List<Category> findRootCategories();

    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId AND c.isActive = true ORDER BY c.displayOrder ASC")
    List<Category> findSubcategoriesByParentId(@Param("parentId") Long parentId);

    @Query("SELECT COUNT(p) FROM Photo p WHERE :category MEMBER OF p.categories")
    long countPhotosInCategory(@Param("category") Category category);

    boolean existsByName(String name);

    boolean existsBySlug(String slug);
}
