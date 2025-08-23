package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.entity.Country;
import com.example.wildlife_backend.repository.CountryRepository;
import com.example.wildlife_backend.service.CountryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public Country createCountry(Country country) {
        // Validate input
        if (country == null || country.getName() == null || country.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Country name cannot be null or empty");
        }
        // Check if country already exists
        if (countryRepository.existsByName(country.getName())) {
            throw new EntityExistsException("Country with name '" + country.getName() + "' already exists");
        }
        // Save and return the country
        return countryRepository.save(country);
    }

    @Override
    public List<Country> createCoutryList(List<Country> countryList) {
        countryList.forEach(country -> {
            if (country == null || country.getName() == null || country.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Country name cannot be null or empty");
            }
            if (countryRepository.existsByName(country.getName())) {
                throw new EntityExistsException("Country with name '" + country.getName() + "' already exists");
            }
            countryRepository.save(country);
        });
        return countryRepository.findAll();
    }

    @Override
    public List<Country> getCoutryList() {
        List<Country> countryList = countryRepository.findAll();
        if (countryList.isEmpty()) {
            throw new IllegalArgumentException("Country List is empty");
        }
        return countryList;
    }

    @Override
    public Country updateCountry(Long id, Country countryDetails) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with id " + id));

        // Validate input
        if (countryDetails.getName() == null || countryDetails.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Country name cannot be null or empty");
        }

        country.setName(countryDetails.getName());
        return countryRepository.save(country);
    }

    @Override
    public void deleteCountry(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with id " + id));

        countryRepository.delete(country);
    }
}
