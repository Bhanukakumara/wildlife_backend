package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country,Long> {
    boolean existsByName(@NotBlank(message = "Country name is required") @Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters") String name);
}
