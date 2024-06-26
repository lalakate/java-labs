package com.kate.lab1.controller;

import com.kate.lab1.aop.RequestStats;
import com.kate.lab1.model.University;
import com.kate.lab1.service.UniversityService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/university")
@RequestStats
@CrossOrigin(origins = "*")
public class UniversityController {
  private final UniversityService universityService;


  @GetMapping("/all")
  public List<University> getAllUniversities() {
    return universityService.getAllUniversities();
  }

  @GetMapping("/{id}")
  public University getUniversityById(@PathVariable Long id) {
    return universityService.getUniversityById(id);
  }

  @GetMapping("/in")
  public List<University> getUniversitiesByCountryId(@RequestParam("countryId") Long id) {
    return universityService.getUniversitiesByCountryId(id);
  }

  @PostMapping("/create/{id}")
  public String createUniversity(@PathVariable Long id, @RequestBody University university) {
    return universityService.createUniversity(id, university);
  }

  @PostMapping("/create")
  public ResponseEntity<List<University>> createUniversities(
      @RequestBody List<University> universities) {
    return ResponseEntity.of(
        Optional.ofNullable(universityService.createUniversities(universities)));
  }

  @PutMapping("/update/{id}")
  public String updateUniversity(@PathVariable Long id, @RequestBody University university) {
    return universityService.updateUniversity(id, university);
  }

  @PatchMapping("/addStudentToUniversity/{id}")
  public String addStudentToUniversity(@PathVariable Long id, @RequestParam Long studentId) {
    return universityService.addStudentToUniversity(id, studentId);
  }

  @PatchMapping("/deleteStudentFromUniversity/{id}")
  public String deleteStudentFromUniversity(@PathVariable Long id, @RequestParam Long studentId) {
    return universityService.deleteStudentFromUniversity(id, studentId);
  }

  @DeleteMapping("/delete/{id}")
  public String deleteUniversity(@PathVariable Long id) {
    return universityService.deleteUniversity(id);
  }
}