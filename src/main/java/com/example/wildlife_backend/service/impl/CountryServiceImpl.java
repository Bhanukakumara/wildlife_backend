package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.Country.CountryCreateDto;
import com.example.wildlife_backend.dto.Country.CountryGetDto;
import com.example.wildlife_backend.entity.Address;
import com.example.wildlife_backend.entity.Country;
import com.example.wildlife_backend.repository.CountryRepository;
import com.example.wildlife_backend.service.CountryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public CountryGetDto createCountry(CountryCreateDto countryCreateDto) {

        // Check if a country with the given country code already exists
        if (countryRepository.existsByCode(countryCreateDto.getCode())) {
            throw new EntityExistsException("Country with code " + countryCreateDto.getCode() + " already exists");
        }
        if (countryRepository.existsByName(countryCreateDto.getName())) {
            throw new EntityExistsException("Country with name " + countryCreateDto.getName() + " already exists");
        }

        // Create a new Country entity
        Country country = new Country();
        country.setName(countryCreateDto.getName());
        country.setCode(countryCreateDto.getCode());

        // Save the entity to the database
        Country savedCountry = countryRepository.save(country);

        // Convert the saved entity to CountryGetDto
        return CountryGetDto.builder()
                .id(savedCountry.getId())
                .name(savedCountry.getName())
                .code(savedCountry.getCode())
                .build();
    }

    @Override
    public boolean deleteCountry(Long id) {
        Optional<Country> country = countryRepository.findById(id);
        country.ifPresent(countryRepository::delete);
        return country.isPresent();
    }

    @Override
    public CountryGetDto updateCountry(Long id, CountryCreateDto countryDetails) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country with ID " + id + " not found"));

        // Check for unique constraints
        Country existingCountryByName = countryRepository.findByName(countryDetails.getName());
        if (existingCountryByName != null && !existingCountryByName.getId().equals(id)) {
            throw new IllegalArgumentException("Country name '" + countryDetails.getName() + "' is already in use");
        }
        Country existingCountryByCode = countryRepository.findByCode(countryDetails.getCode());
        if (existingCountryByCode != null && !existingCountryByCode.getId().equals(id)) {
            throw new IllegalArgumentException("Country code '" + countryDetails.getCode() + "' is already in use");
        }

        // Update fields
        country.setName(countryDetails.getName());
        country.setCode(countryDetails.getCode());

        Country updatedCountry = countryRepository.save(country);
        return convertToAddress(updatedCountry);
    }

    @Override
    public List<CountryGetDto> createCountryList(List<CountryCreateDto> countryCreateDto) {
        List<CountryGetDto> countryGetDtoList = new ArrayList<>();
        countryCreateDto.forEach(country -> {
            if (country == null || country.getName() == null || country.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Country name cannot be null or empty");
            }
            if (countryRepository.existsByName(country.getName())) {
                throw new EntityExistsException("Country with name '" + country.getName() + "' already exists");
            }
            countryGetDtoList.add(convertToAddress(countryRepository.save(Country.builder()
                    .name(country.getName())
                    .code(country.getCode())
                    .build()
            )));
            log.info("Country created with code {} and name {}", country.getCode(), country.getName());
        });
        return countryGetDtoList;
    }

    @Override
    public List<CountryGetDto> getCountryList() {
        List<CountryGetDto> countryGetDtoList = new ArrayList<>();
        countryRepository.findAll().forEach(country -> countryGetDtoList.add(convertToAddress(country)));
        return countryGetDtoList;
    }

    @Override
    public CountryGetDto getCountryById(Long id) {
        Optional<Country> country = countryRepository.findById(id);
        if (country.isPresent()) {
            return convertToAddress(country.get());
        }
        throw new EntityNotFoundException("Country with id " + id + " not found");
    }

    @Override
    public CountryGetDto getCountryByCode(String code) {
        Country country = countryRepository.findByCode(code);
        if (country == null) {
            throw new EntityNotFoundException("Country with code " + code + " not found");
        }
        return convertToAddress(country);
    }

    @Override
    public List<CountryGetDto> searchCountriesByName(String name) {
        List<CountryGetDto> countryGetDtoList = new ArrayList<>();
        List<Country> countryList = countryRepository.findByNameContainingIgnoreCase(name.trim());
        if (countryList.isEmpty()) {
            throw new EntityNotFoundException("Country with name " + name + " not found");
        }
        for (Country country : countryList) {
            countryGetDtoList.add(convertToAddress(country));
        }
        return countryGetDtoList;
    }

    @Override
    public boolean validateCountry(CountryCreateDto countryCreateDto) {
        return false;
    }

    private CountryGetDto convertToAddress(Country country) {
        return CountryGetDto.builder()
                .id(country.getId())
                .name(country.getName())
                .code(country.getCode())
                .build();
    }
}
