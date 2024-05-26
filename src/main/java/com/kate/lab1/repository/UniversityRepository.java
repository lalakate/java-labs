package com.kate.lab1.repository;

import com.kate.lab1.model.University;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
  List<University> getUniversitiesByCountryId(Long countryId);
}