package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.Country.CountryCreateDto;
import com.example.wildlife_backend.dto.Country.CountryGetDto;
import com.example.wildlife_backend.entity.Country;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    CountryGetDto createCountry(CountryCreateDto countryCreateDto);
    boolean deleteCountry(Long id);
    CountryGetDto updateCountry(Long id, CountryCreateDto countryDetails);

    List<CountryGetDto> createCountryList(@Valid List<CountryCreateDto> countryCreateDto);

    List<CountryGetDto> getCountryList();

    CountryGetDto getCountryById(Long id);

    CountryGetDto getCountryByCode(String code);

    List<CountryGetDto> searchCountriesByName(String name);

    boolean validateCountry(@Valid CountryCreateDto countryCreateDto);
}
