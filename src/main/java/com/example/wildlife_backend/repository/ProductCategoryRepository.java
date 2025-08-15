package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    
    Optional<ProductCategory> findByName(String name);
    
    boolean existsByName(String name);
    
    List<ProductCategory> findByActive(boolean active);
    
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.name LIKE %:keyword% OR pc.description LIKE %:keyword%")
    List<ProductCategory> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.active = true ORDER BY pc.name")
    List<ProductCategory> findAllActiveCategories();
}
