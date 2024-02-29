package com.kate.lab1.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UniversityResponse {
    String name;
    @JsonProperty("web_pages")
    public List<String> webPages;
    List<String> domains;
}