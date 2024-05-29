package com.kate.lab1.service;

import com.kate.lab1.aop.Logging;
import com.kate.lab1.cache.RequestCache;
import com.kate.lab1.exception.NotFoundException;
import com.kate.lab1.model.Country;
import com.kate.lab1.model.University;
import com.kate.lab1.repository.CountryRepository;
import com.kate.lab1.repository.UniversityRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class CountryService {
  private static final String ALL_COUNTRIES_REQUEST = "http://localhost:8080/api/v1/country/all";
  private static final String COUNTRY_BY_ID_REQUEST = "http://localhost:8080/api/v1/country/";

  private CountryRepository countryRepository;
  private UniversityRepository universityRepository;

  private static final String COUNTRY_ERROR_MESSAGE = "There is no country with id = ";

  @Logging
  public List<Country> getAllCountries() {
    if (RequestCache.containsKey(ALL_COUNTRIES_REQUEST)) {
      return (List<Country>) RequestCache.get(ALL_COUNTRIES_REQUEST);
    } else {
      List<Country> countries = countryRepository.findAll();
      RequestCache.put(ALL_COUNTRIES_REQUEST, countries);
      return countries;
    }
  }

  @Logging
  public Country getCountryById(Long id) {
    if (RequestCache.containsKey(COUNTRY_BY_ID_REQUEST + id.toString())) {
      return ((List<Country>) RequestCache.get(COUNTRY_BY_ID_REQUEST + id)).get(0);
    } else {
      Country country = countryRepository.findById(id).orElse(null);

      if (country == null) {
        throw new NotFoundException(COUNTRY_ERROR_MESSAGE, id);
      }

      List<Country> countries = new ArrayList<>();
      countries.add(country);
      RequestCache.put(COUNTRY_BY_ID_REQUEST + id, countries);
      return country;
    }
  }

  @Logging
  @Transactional
  public String createCountry(Country country) {
    List<University> universityList = country.getUniversities();
    for (University university : universityList) {
      university.setCountry(country);
    }

    countryRepository.save(country);

    universityRepository.saveAll(universityList);

    RequestCache.clear();

    return "Successfully created!";
  }

  @Logging
  public String updateCountry(Long id, String countryName) {
    Country country = countryRepository.findById(id).orElse(null);
    if (country != null) {
      country.setName(countryName);
      countryRepository.save(country);

      RequestCache.clear();

      return "Successfully updated!";
    } else {
      throw new NotFoundException(COUNTRY_ERROR_MESSAGE, id);
    }
  }

  @Logging
  @Transactional
  public String deleteCountry(Long id) {
    Country country = countryRepository.findById(id).orElse(null);
    if (country == null) {
      throw new NotFoundException(COUNTRY_ERROR_MESSAGE, id);
    } else {

      List<University> universityList = new ArrayList<>(country.getUniversities());
      universityRepository.deleteAll(universityList);
      countryRepository.delete(country);

      RequestCache.clear();

      return "Successfully deleted!";
    }
  }
}