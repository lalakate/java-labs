package com.kate.lab1.controller;

import com.kate.lab1.payload.UniversityResponse;
import com.kate.lab1.service.UniversityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/v1/university")

public class UniversityController {
    UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping
    public ResponseEntity<List<UniversityResponse>> getUniversities(@RequestParam(value = "country") String country) {
        if (Pattern.compile("\\d").matcher(country).find()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        List<UniversityResponse> universityController = universityService.getUniversity(country);
        return new ResponseEntity<>(universityController, HttpStatus.OK);
    }
}