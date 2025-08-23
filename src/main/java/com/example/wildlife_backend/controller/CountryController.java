package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.entity.Country;
import com.example.wildlife_backend.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/country/")
@CrossOrigin
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @PostMapping("create")
    public ResponseEntity<Country> createCountry(@RequestBody Country country) {
        return new ResponseEntity<>(countryService.createCountry(country), HttpStatus.CREATED);
    }

    @PostMapping("create-country-list")
    public ResponseEntity<List<Country>> createCountryList(@RequestBody List<Country> countryList) {
        return new ResponseEntity<>(countryService.createCoutryList(countryList), HttpStatus.CREATED);
    }

    @GetMapping("get-all")
    public ResponseEntity<List<Country>> getAllCountry() {
        return new ResponseEntity<>(countryService.getCoutryList(), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable Long id, @RequestBody Country country) {
        return new ResponseEntity<>(countryService.updateCountry(id, country), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
