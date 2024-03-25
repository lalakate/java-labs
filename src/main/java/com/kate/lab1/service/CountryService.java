package com.kate.lab1.service;

import com.kate.lab1.model.Country;
import com.kate.lab1.model.University;
import com.kate.lab1.repository.CountryRepository;
import com.kate.lab1.repository.UniversityRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CountryService {
    private CountryRepository countryRepository;
    private UniversityRepository universityRepository;

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Country getCountryById(Long id) {
        return countryRepository.findById(id).orElse(null);
    }

    @Transactional
    public String createCountry(Country country) {
        List<University> universityList = country.getUniversities();
        for (University university : universityList) {
            university.setCountry(country);
        }

        countryRepository.save(country);

        universityRepository.saveAll(universityList);

        return "Successfully created!";
    }

    public String updateCountry(Long id, String countryName) {
        Country country = countryRepository.findById(id).orElse(null);
        if (country != null) {
            country.setName(countryName);
            countryRepository.save(country);

            return "Successfully updated!";
        } else return "Wrong id!";
    }

    public String deleteCountry(Long id) {
        Country country = countryRepository.findById(id).orElse(null);
        if (country == null)
            return "Wrong id!";
        else {

            List<University> universityList = new ArrayList<>(country.getUniversities());
            universityRepository.deleteAll(universityList);
            countryRepository.delete(country);
            return "Successfully deleted!";
        }
    }
}