package com.kate.lab1.repository;

import com.kate.lab1.model.Student;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
  @Query("SELECT s FROM Student s WHERE s.surname = :surname")
  Student findStudentBySurname(@Param("surname") String surname);
  List<Student> findByUniversitiesId(Long universityId);
}