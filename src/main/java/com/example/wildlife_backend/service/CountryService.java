package com.example.wildlife_backend.service;

import com.example.wildlife_backend.entity.Country;

import java.util.List;

public interface CountryService {
    Country createCountry(Country country);
    List<Country> createCoutryList(List<Country> countryList);
    List<Country> getCoutryList();
    Country updateCountry(Country country);
    void deleteCountry(Long id);
}
