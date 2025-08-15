package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findByName(String name);
    
    boolean existsByName(String name);
    
    List<Product> findByActive(boolean active);
    
    List<Product> findByFeatured(boolean featured);
    
    List<Product> findByCategoryId(Long categoryId);
    
    List<Product> findByCategoryIdAndActive(Long categoryId, boolean active);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Product> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.featured = true")
    List<Product> findFeaturedActiveProducts();
    
    @Query("SELECT p FROM Product p WHERE p.active = true ORDER BY p.createdAt DESC")
    Page<Product> findAllActiveProducts(Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.active = true")
    List<Product> findActiveProductsByCategory(@Param("categoryId") Long categoryId);
}
