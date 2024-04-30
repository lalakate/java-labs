package com.kate.lab1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kate.lab1.model.Student;
import com.kate.lab1.model.University;
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

class StudentServiceTest {

  @Mock
  StudentRepository studentRepository;

  @Mock
  UniversityRepository universityRepository;

  @InjectMocks
  StudentService studentService;

  Student student;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    student = new Student();
    student.setSurname("Test Student");
    student.setName("Test Student");
    student.setFaculty("Test Student");
    student.setNumber(1L);
    student.setId(1L);
    student.setSpecialization("Test Student");
    student.setUniversities(new ArrayList<>());
  }

  @Test
  void testGetAllStudents() {
    List<Student> students = Arrays.asList(student, student, student);
    when(studentRepository.findAll()).thenReturn(students);

    List<Student> result = studentService.getAllStudents();

    assertEquals(students, result);
    assertEquals(3, result.size());
    verify(studentRepository, times(1)).findAll();
  }

  @Test
  void testGetStudentById() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

    Student result = studentService.getStudentById(1L);

    assertEquals(student, result);
    verify(studentRepository, times(1)).findById(1L);
  }

  @Test
  void testCreateStudent() {
    List<Long> universityIds = new ArrayList<>();
    universityIds.add(1L);

    University university = new University();
    university.setId(1L);
    university.setName("Test University");
    university.setStudents(new ArrayList<>());

    List<University> universities = new ArrayList<>();
    universities.add(university);

    when(universityRepository.findAllById(universityIds)).thenReturn(universities);
    when(studentRepository.save(any(Student.class))).thenReturn(student);
    when(universityRepository.save(any(University.class))).thenReturn(university);

    String result = studentService.createStudent(universityIds, student);

    assertEquals("Successfully created!", result);
    verify(studentRepository, times(1)).save(any(Student.class));
    verify(universityRepository, times(1)).save(any(University.class));
  }

  @Test
  void testUpdateStudent() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
    when(studentRepository.save(any(Student.class))).thenReturn(student);

    String result = studentService.updateStudent(1L, student);

    assertEquals("Successfully updated!", result);
    verify(studentRepository, times(1)).save(any(Student.class));
  }

  @Test
  void testDeleteStudent() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

    String result = studentService.deleteStudent(1L);

    assertEquals("Successfully deleted!", result);
    verify(studentRepository, times(1)).delete(any(Student.class));
  }

  @Test
  void testAddUniversityToStudent() {
    University university = new University();
    university.setId(1L);
    university.setName("Test University");
    university.setStudents(new ArrayList<>());

    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
    when(universityRepository.findById(1L)).thenReturn(Optional.of(university));

    String result = studentService.addUniversityToStudent(1L, 1L);

    assertEquals("Successfully added!", result);
    verify(studentRepository, times(1)).save(any(Student.class));
    verify(universityRepository, times(1)).save(any(University.class));
  }

  @Test
  void testDeleteUniversityFromStudent() {
    University university = new University();
    university.setId(1L);
    university.setName("Test University");
    university.setStudents(new ArrayList<>());
    university.getStudents().add(student);
    student.getUniversities().add(university);

    when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
    when(universityRepository.findById(1L)).thenReturn(Optional.of(university));

    String result = studentService.deleteUniversityFromStudent(1L, 1L);

    assertEquals("Successfully deleted!", result);
    verify(studentRepository, times(1)).save(any(Student.class));
    verify(universityRepository, times(1)).save(any(University.class));
    student.getUniversities().remove(university);
  }
}