package com.kate.lab1.controller;

import com.kate.lab1.model.Country;
import com.kate.lab1.service.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/university/country")
public class CountryController {
    private final CountryService countryService;

    @GetMapping("/all")
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Long id) {
        return countryService.getCountryById(id);
    }

    @PostMapping("/create")
    public String createCountry(@RequestBody Country country) {
        return countryService.createCountry(country);
    }

    @PutMapping("/update/{id}")
    public String updateCountry(@PathVariable Long id, @RequestParam String name) {
        return countryService.updateCountry(id, name);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCountry(@PathVariable Long id) {
        return countryService.deleteCountry(id);
    }
}