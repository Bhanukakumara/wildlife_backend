package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.Country.CountryCreateDto;
import com.example.wildlife_backend.dto.Country.CountryGetDto;
import com.example.wildlife_backend.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/country/")
@CrossOrigin
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    // Create a new country
    @PostMapping("/create")
    public ResponseEntity<CountryGetDto> createCountry(@Valid @RequestBody CountryCreateDto countryCreateDto) {
        try {
            CountryGetDto createdCountry = countryService.createCountry(countryCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCountry);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    // Bulk create countries
    @PostMapping("/create-country-list")
    public ResponseEntity<List<CountryGetDto>> createCountryList(@Valid @RequestBody List<CountryCreateDto> countryCreateDto) {
        List<CountryGetDto> createdCountries = countryService.createCountryList(countryCreateDto);
        return new ResponseEntity<>(createdCountries, HttpStatus.CREATED);
    }

    // Get all countries
    @GetMapping("/get-all")
    public ResponseEntity<List<CountryGetDto>> getAllCountries() {
        List<CountryGetDto> countries = countryService.getCountryList();
        if (countries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    // Get country by ID
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<CountryGetDto> getCountryById(@PathVariable Long id) {
        CountryGetDto country = countryService.getCountryById(id);
        if (country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(country);
    }

    // Get country by country code
    @GetMapping("/get-by-code/{code}")
    public ResponseEntity<CountryGetDto> getCountryByCode(@PathVariable String code) {
        CountryGetDto country = countryService.getCountryByCode(code);
        if (country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(country);
    }

    // Search countries by name (partial match)
    @GetMapping("/search")
    public ResponseEntity<List<CountryGetDto>> searchCountriesByName(@RequestParam String name) {
        List<CountryGetDto> countries = countryService.searchCountriesByName(name);
        if (countries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    // Update a country
    @PutMapping("/update/{id}")
    public ResponseEntity<CountryGetDto> updateCountry(@PathVariable Long id, @Valid @RequestBody CountryCreateDto countryCreateDto) {
        CountryGetDto updatedCountry = countryService.updateCountry(id, countryCreateDto);
        if (updatedCountry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedCountry);
    }

    // Delete a country
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteCountry(@PathVariable Long id) {
        boolean deleted = countryService.deleteCountry(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Validate country data
    @PostMapping("/validate")
    public ResponseEntity<String> validateCountry(@Valid @RequestBody CountryCreateDto countryCreateDto) {
        boolean isValid = countryService.validateCountry(countryCreateDto);
        if (isValid) {
            return new ResponseEntity<>("Country data is valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid country data", HttpStatus.BAD_REQUEST);
    }
}
