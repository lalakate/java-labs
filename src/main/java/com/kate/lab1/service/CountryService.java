package com.kate.lab1.service;

import com.kate.lab1.cache.RequestCache;
import com.kate.lab1.model.Country;
import com.kate.lab1.model.University;
import com.kate.lab1.repository.CountryRepository;
import com.kate.lab1.repository.UniversityRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class CountryService {
    private static final String ALL_COUNTRIES_REQUEST = "http://localhost:8080/api/v1/country/all";
    private static final String COUNTRY_BY_ID_REQUEST = "http://localhost:8080/api/v1/country/";
    private static final Logger LOGGER = Logger.getLogger(CountryService.class.getName());

    private CountryRepository countryRepository;
    private UniversityRepository universityRepository;

    public List<Country> getAllCountries() {
        if(RequestCache.containsKey(ALL_COUNTRIES_REQUEST)) {
            LOGGER.info("Getting all countries from cache");
            return (List<Country>) RequestCache.get(ALL_COUNTRIES_REQUEST);
        }
        else {
            List<Country> countries = countryRepository.findAll();
            RequestCache.put(ALL_COUNTRIES_REQUEST, countries);
            LOGGER.info("Getting all countries from cache");
            return countries;
        }
    }

    public Country getCountryById(Long id) {
        if(RequestCache.containsKey(COUNTRY_BY_ID_REQUEST + id.toString())) {
            LOGGER.info("Getting country by id from cache");
            return ((List<Country>)RequestCache.get(COUNTRY_BY_ID_REQUEST + id)).get(0);
        }
        else {
            Country country = countryRepository.findById(id).orElse(null);
            List<Country> countries = new ArrayList<>();
            countries.add(country);
            RequestCache.put(COUNTRY_BY_ID_REQUEST + id, countries);
            LOGGER.info("Getting country by id from cache");
            return country;
        }
    }

    @Transactional
    public String createCountry(Country country) {
        List<University> universityList = country.getUniversities();
        for (University university : universityList) {
            university.setCountry(country);
        }

        countryRepository.save(country);

        universityRepository.saveAll(universityList);

        RequestCache.clear();
        LOGGER.info("Cache cleared in function createCountry");

        return "Successfully created!";
    }

    public String updateCountry(Long id, String countryName) {
        Country country = countryRepository.findById(id).orElse(null);
        if (country != null) {
            country.setName(countryName);
            countryRepository.save(country);

            RequestCache.clear();
            LOGGER.info("Cache cleared in function updateCountry");

            return "Successfully updated!";
        } else return "Wrong id!";
    }

    @Transactional
    public String deleteCountry(Long id) {
        Country country = countryRepository.findById(id).orElse(null);
        if (country == null)
            return "Wrong id!";
        else {

            List<University> universityList = new ArrayList<>(country.getUniversities());
            universityRepository.deleteAll(universityList);
            countryRepository.delete(country);

            RequestCache.clear();
            LOGGER.info("Cache cleared in function deleteCountry");

            return "Successfully deleted!";
        }
    }
}