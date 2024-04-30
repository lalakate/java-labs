package com.kate.lab1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kate.lab1.model.Country;
import com.kate.lab1.repository.CountryRepository;
import com.kate.lab1.repository.UniversityRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CountryServiceTest {

  @Mock
  CountryRepository countryRepository;

  @Mock
  UniversityRepository universityRepository;

  @InjectMocks
  CountryService countryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllCountries() {
    Country country = new Country();
    country.setId(1L);
    country.setName("Test Country");

    List<Country> countries = Arrays.asList(country, country, country);
    when(countryRepository.findAll()).thenReturn(countries);

    List<Country> result = countryService.getAllCountries();
    assertEquals(countries, result);
    verify(countryRepository, times(1)).findAll();

    result = countryService.getAllCountries();
    assertEquals(countries, result);
    verify(countryRepository, times(1)).findAll();
  }

  @Test
  void testGetCountryById() {
    Country country = new Country();
    country.setId(1L);
    country.setName("Test Country");
    country.setUniversities(new ArrayList<>());

    when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

    Country result = countryService.getCountryById(1L);
    assertEquals(country, result);
    verify(countryRepository, times(1)).findById(1L);

    result = countryService.getCountryById(1L);
    assertEquals(country, result);
    verify(countryRepository, times(1)).findById(1L);

    Exception exception = assertThrows(RuntimeException.class, () -> {
      countryService.getCountryById(2L);
    });

    String expectedMessage = "There is no country with id = 2";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }


  @Test
  void testCreateCountry() {
    Country country = new Country();
    country.setId(1L);
    country.setName("Test Country");
    country.setUniversities(new ArrayList<>());

    when(countryRepository.save(any(Country.class))).thenReturn(country);

    String result = countryService.createCountry(country);

    assertEquals("Successfully created!", result);
    verify(countryRepository, times(1)).save(any(Country.class));
  }

  @Test
  void testUpdateCountry() {
    Country country = new Country();
    country.setId(1L);
    country.setName("Test Country");

    when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
    when(countryRepository.save(any(Country.class))).thenReturn(country);

    String result = countryService.updateCountry(1L, "Updated Country");

    assertEquals("Successfully updated!", result);
    verify(countryRepository, times(1)).save(any(Country.class));
  }

  @Test
  void testDeleteCountry() {
    Country country = new Country();
    country.setId(1L);
    country.setName("Test Country");
    country.setUniversities(new ArrayList<>());

    when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

    String result = countryService.deleteCountry(1L);

    assertEquals("Successfully deleted!", result);
    verify(countryRepository, times(1)).delete(any(Country.class));
  }
}