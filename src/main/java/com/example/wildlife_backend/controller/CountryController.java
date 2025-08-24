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
        CountryGetDto createdCountry = countryService.createCountry(countryCreateDto);
        return new ResponseEntity<>(createdCountry, HttpStatus.CREATED);
    }

    // Bulk create countries
    @PostMapping("/create-country-list")
    public ResponseEntity<List<CountryGetDto>> createCountryList(@Valid @RequestBody List<CountryCreateDto> countryCreateDtos) {
        List<CountryGetDto> createdCountries = countryService.createCountryList(countryCreateDtos);
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
        Optional<CountryGetDto> country = countryService.getCountryById(id);
        return country.map(countryDto -> new ResponseEntity<>(countryDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get country by country code
    @GetMapping("/get-by-code/{countryCode}")
    public ResponseEntity<CountryGetDto> getCountryByCode(@PathVariable String countryCode) {
        Optional<CountryGetDto> country = countryService.getCountryByCode(countryCode);
        return country.map(countryDto -> new ResponseEntity<>(countryDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
        Optional<CountryGetDto> updatedCountry = countryService.updateCountry(id, countryCreateDto);
        return updatedCountry.map(country -> new ResponseEntity<>(country, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete a country
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
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

    // Get countries with associated addresses
    @GetMapping("/get-with-addresses")
    public ResponseEntity<List<CountryGetDto>> getCountriesWithAddresses() {
        List<CountryGetDto> countries = countryService.getCountriesWithAddresses();
        if (countries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }
}
