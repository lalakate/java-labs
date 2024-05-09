package com.kate.lab1.service;

import com.kate.lab1.aop.Logging;
import com.kate.lab1.cache.RequestCache;
import com.kate.lab1.exception.BadRequestException;
import com.kate.lab1.exception.NotFoundException;
import com.kate.lab1.model.Country;
import com.kate.lab1.model.Student;
import com.kate.lab1.model.University;
import com.kate.lab1.repository.CountryRepository;
import com.kate.lab1.repository.StudentRepository;
import com.kate.lab1.repository.UniversityRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UniversityService {
  private static final String ALL_UNIVERSITIES_REQUEST =
      "http://localhost:8080/api/v1/university/all";
  private static final String UNIVERSITY_BY_ID_REQUEST = "http://localhost:8080/api/v1/university/";

  private UniversityRepository universityRepository;
  private CountryRepository countryRepository;
  private StudentRepository studentRepository;

  private static final String UNIVERSITY_ERROR_MESSAGE = "There is no university with id = ";
  private static final String COUNTRY_ERROR_MESSAGE = "There is no country with id = ";
  private static final String STUDENT_ERROR_MESSAGE = "There is no student with id = ";

  private static final String BAD_REQUEST_MESSAGE = "Bad request.";

  @Logging
  public List<University> getAllUniversities() {
    if (RequestCache.containsKey(ALL_UNIVERSITIES_REQUEST)) {
      return (List<University>) RequestCache.get(ALL_UNIVERSITIES_REQUEST);
    }
    List<University> universities = universityRepository.findAll();
    RequestCache.put(ALL_UNIVERSITIES_REQUEST, universities);
    return universities;
  }

  @Logging
  public University getUniversityById(Long id) {
    if (RequestCache.containsKey(UNIVERSITY_BY_ID_REQUEST + id)) {
      return ((List<University>) RequestCache.get(UNIVERSITY_BY_ID_REQUEST + id)).get(0);
    } else {
      University university = universityRepository.findById(id).orElse(null);

      if (university == null) {
        throw new NotFoundException(UNIVERSITY_ERROR_MESSAGE, id);
      }

      List<University> universities = new ArrayList<>();
      universities.add(university);
      RequestCache.put(UNIVERSITY_BY_ID_REQUEST + id, universities);
      return university;
    }
  }

  @Logging
  public String createUniversity(Long countryId, University university) {
    Set<Student> uniqueStudents = new HashSet<>();
    for (Student student : university.getStudents()) {
      if (!uniqueStudents.add(student)) {
        return "Bad request, do not share the same students in the same university";
      }
    }

    List<Student> students = university.getStudents();
    List<Student> managedStudents = new ArrayList<>();

    for (Student student : students) {
      Student existingStudent = studentRepository.findStudentBySurname(student.getSurname());

      if (existingStudent != null) {
        managedStudents.add(existingStudent);
      } else {
        studentRepository.save(student);
        managedStudents.add(student);
      }
    }

    Country country = countryRepository.findById(countryId).orElse(null);
    if (country == null) {
      throw new NotFoundException(COUNTRY_ERROR_MESSAGE, countryId);
    } else {
      University newUniversity = new University();
      newUniversity.setName(university.getName());
      newUniversity.setDomain(university.getDomain());
      newUniversity.setIndex(university.getIndex());
      newUniversity.setWebPage(university.getWebPage());
      newUniversity.setCountry(country);
      newUniversity.setStudents(managedStudents);

      country.getUniversities().add(newUniversity);

      universityRepository.save(newUniversity);
      countryRepository.save(country);

      RequestCache.clear();

      return "Successfully created!";
    }
  }

  public List<University> createUniversities(List<University> universities) {
    return universities.stream().map(universityRepository::save).toList();
  }

  @Logging
  public String updateUniversity(Long id, University university) {
    University newUniversity = universityRepository.findById(id).orElse(null);

    if (newUniversity == null) {
      throw new NotFoundException(UNIVERSITY_ERROR_MESSAGE, id);
    } else {
      newUniversity.setName(university.getName());
      newUniversity.setDomain(university.getDomain());
      newUniversity.setIndex(university.getIndex());
      newUniversity.setWebPage(university.getWebPage());
      newUniversity.setCountry(university.getCountry());
      newUniversity.setStudents(university.getStudents());
      universityRepository.save(newUniversity);

      RequestCache.clear();

      return "Successfully updated!";
    }
  }

  @Logging
  public String deleteUniversity(Long id) {
    Optional<University> optionalUniversity = universityRepository.findById(id);
    if (optionalUniversity.isPresent()) {
      universityRepository.deleteById(id);

      RequestCache.clear();

      return "Successfully deleted!";
    } else {
      throw new NotFoundException(UNIVERSITY_ERROR_MESSAGE, id);
    }
  }

  @Logging
  public String addStudentToUniversity(Long universityId, Long studentId) {
    University university = universityRepository.findById(universityId).orElse(null);
    Student student = studentRepository.findById(studentId).orElse(null);

    if (university == null || student == null) {
      throw new BadRequestException(BAD_REQUEST_MESSAGE);
    }

    if (!university.getStudents().contains(student)) {
      student.getUniversities().add(university);
      university.getStudents().add(student);
      studentRepository.save(student);
      universityRepository.save(university);

      RequestCache.clear();

      return "Successfully added!";
    } else {
      return "Wrong id or this student exists in university.";
    }
  }

  @Logging
  public String deleteStudentFromUniversity(Long universityId, Long studentId) {
    University university = universityRepository.findById(universityId).orElse(null);
    Student student = studentRepository.findById(studentId).orElse(null);

    if (university == null) {
      throw new NotFoundException(UNIVERSITY_ERROR_MESSAGE, universityId);
    } else if (student == null) {
      throw new NotFoundException(STUDENT_ERROR_MESSAGE, studentId);
    }

    if (university.getStudents().contains(student) &&
        student.getUniversities().contains(university)) {
      student.getUniversities().remove(university);
      university.getStudents().remove(student);
      studentRepository.save(student);
      universityRepository.save(university);

      RequestCache.clear();

      return "Successfully deleted!";
    } else {
      return "Wrong id or this university does not contain this student.";
    }
  }
}