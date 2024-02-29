package com.kate.lab1.service;

import com.kate.lab1.payload.UniversityResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UniversityService {
    public List<UniversityResponse> getUniversity(String country) {
        String apiUrl = "http://universities.hipolabs.com/search?country={country}";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UniversityResponse[]> responseEntity = restTemplate.getForEntity(apiUrl, UniversityResponse[].class, country);
        UniversityResponse[] responseBody = responseEntity.getBody();
        if (responseBody != null && responseBody.length > 0) {
            List<UniversityResponse> universities = new ArrayList<>();
            Collections.addAll(universities, responseBody);
            return universities;
        } else return Collections.emptyList();
    }
}