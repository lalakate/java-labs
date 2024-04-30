package com.kate.lab1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kate.lab1.model.Country;
import com.kate.lab1.model.Student;
import com.kate.lab1.model.University;
import com.kate.lab1.repository.CountryRepository;
import com.kate.lab1.repository.StudentRepository;
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

class UniversityServiceTest {

  @Mock
  UniversityRepository universityRepository;

  @Mock
  CountryRepository countryRepository;

  @Mock
  StudentRepository studentRepository;

  @InjectMocks
  UniversityService universityService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllUniversities() {
    University university = new University();
    university.setId(1L);
    university.setName("Test University");

    List<University> universities = Arrays.asList(university, university, university);
    when(universityRepository.findAll()).thenReturn(universities);

    List<University> result = universityService.getAllUniversities();
    assertEquals(universities, result);
    verify(universityRepository, times(1)).findAll();

    result = universityService.getAllUniversities();
    assertEquals(universities, result);
    verify(universityRepository, times(1)).findAll();
  }

  @Test
  void testGetUniversityById() {
    University university = new University();
    university.setId(1L);
    university.setName("Test University");

    when(universityRepository.findById(1L)).thenReturn(Optional.of(university));

    University result = universityService.getUniversityById(1L);
    assertEquals(university, result);
    verify(universityRepository, times(1)).findById(1L);

    result = universityService.getUniversityById(1L);
    assertEquals(university, result);
    verify(universityRepository, times(1)).findById(1L);

    Exception exception = assertThrows(RuntimeException.class, () -> {
      universityService.getUniversityById(2L);
    });

    String expectedMessage = "There is no university with id = 2";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);

  }

  @Test
  void testCreateUniversity() {
    Country country = new Country();
    country.setId(1L);
    country.setName("Test Country");

    University university = new University();
    university.setId(1L);
    university.setName("Test University");

    university.setStudents(new ArrayList<>());
    country.setUniversities(new ArrayList<>());

    when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
    when(universityRepository.save(any(University.class))).thenReturn(university);

    String result = universityService.createUniversity(1L, university);

    assertEquals("Successfully created!", result);
    verify(universityRepository, times(1)).save(any(University.class));
  }

  @Test
  void testUpdateUniversity() {
    University university = new University();
    university.setId(1L);
    university.setName("Test University");

    when(universityRepository.findById(1L)).thenReturn(Optional.of(university));
    when(universityRepository.save(any(University.class))).thenReturn(university);

    String result = universityService.updateUniversity(1L, university);

    assertEquals("Successfully updated!", result);
    verify(universityRepository, times(1)).save(any(University.class));
  }

  @Test
  void testDeleteUniversity() {
    University university = new University();
    university.setId(1L);
    university.setName("Test University");

    when(universityRepository.findById(1L)).thenReturn(Optional.of(university));

    String result = universityService.deleteUniversity(1L);

    assertEquals("Successfully deleted!", result);
    verify(universityRepository, times(1)).deleteById(any(Long.class));
  }

  @Test
  void testAddStudentToUniversity() {
    University university = new University();
    university.setId(1L);
    university.setName("Test University");

    Student student = new Student();
    student.setId(1L);
    student.setName("Test Student");

    university.setStudents(new ArrayList<>());
    student.setUniversities(new ArrayList<>());

    when(universityRepository.findById(1L)).thenReturn(Optional.of(university));
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

    String result = universityService.addStudentToUniversity(1L, 1L);

    assertEquals("Successfully added!", result);
    verify(universityRepository, times(1)).save(any(University.class));
    verify(studentRepository, times(1)).save(any(Student.class));
  }

  @Test
  void testDeleteStudentFromUniversity() {
    University university = new University();
    university.setId(1L);
    university.setName("Test University");

    Student student = new Student();
    student.setId(1L);
    student.setName("Test Student");

    List<Student> students = new ArrayList<>();
    students.add(student);
    university.setStudents(students);

    List<University> universities = new ArrayList<>();
    universities.add(university);
    student.setUniversities(universities);

    when(universityRepository.findById(1L)).thenReturn(Optional.of(university));
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

    String result = universityService.deleteStudentFromUniversity(1L, 1L);

    assertEquals("Successfully deleted!", result);
    verify(universityRepository, times(1)).save(any(University.class));
    verify(studentRepository, times(1)).save(any(Student.class));
  }
}