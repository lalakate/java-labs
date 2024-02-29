package com.kate.lab1.payload;

import lombok.Data;

import java.util.List;

@Data
public class UniversityReport {
    List<UniversityResponse> universities;
}