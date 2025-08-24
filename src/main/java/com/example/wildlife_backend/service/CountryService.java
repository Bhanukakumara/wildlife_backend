package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.Country.CountryCreateDto;
import com.example.wildlife_backend.dto.Country.CountryGetDto;
import com.example.wildlife_backend.entity.Country;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    CountryGetDto createCountry(CountryCreateDto countryCreateDto);
    List<Country> createCoutryList(List<Country> countryList);
    List<Country> getCoutryList();
    Country updateCountry(Country country);
    boolean deleteCountry(Long id);
    Optional<CountryGetDto> updateCountry(Long id, CountryCreateDto countryDetails);

    List<CountryGetDto> createCountryList(@Valid List<CountryCreateDto> countryCreateDtos);

    List<CountryGetDto> getCountryList();

    Optional<CountryGetDto> getCountryById(Long id);

    Optional<CountryGetDto> getCountryByCode(String countryCode);

    List<CountryGetDto> searchCountriesByName(String name);

    boolean validateCountry(@Valid CountryCreateDto countryCreateDto);

    List<CountryGetDto> getCountriesWithAddresses();
}
