package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country,Long> {
    Country findByCode(String code);

    boolean existsByName(String name);

    boolean existsByCode(String code);

    List<Country> findByNameContainingIgnoreCase(String trim);

    Country findByName(String name);
}
