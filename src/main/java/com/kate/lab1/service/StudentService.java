package com.kate.lab1.service;

import com.kate.lab1.cache.RequestCache;
import com.kate.lab1.model.Student;
import com.kate.lab1.model.University;
import com.kate.lab1.repository.StudentRepository;
import com.kate.lab1.repository.UniversityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class StudentService {
    private static final String ALL_STUDENTS_REQUEST = "http://localhost:8080/api/v1/student/all";
    private static final String STUDENT_BY_ID_REQUEST = "http://localhost:8080/api/v1/student/";

    private StudentRepository studentRepository;
    private UniversityRepository universityRepository;

    private static final Logger LOGGER = Logger.getLogger(StudentService.class.getName());

    public List<Student> getAllStudents() {
        if (RequestCache.containsKey(ALL_STUDENTS_REQUEST)) {
            LOGGER.info("Getting all students from cache");
            return (List<Student>) RequestCache.get(ALL_STUDENTS_REQUEST);
        } else {
            List<Student> students = studentRepository.findAll();
            RequestCache.put(ALL_STUDENTS_REQUEST, students);
            LOGGER.info("Getting all students from database");
            return students;
        }
    }

    public Student getStudentById(Long id) {
        if (RequestCache.containsKey(STUDENT_BY_ID_REQUEST + id)) {
            LOGGER.info("Getting student by id from cache");
            return ((List<Student>) RequestCache.get(STUDENT_BY_ID_REQUEST + id)).get(0);
        } else {
            Student student = studentRepository.findById(id).orElse(null);
            List<Student> students = new ArrayList<>();
            students.add(student);

            RequestCache.put(STUDENT_BY_ID_REQUEST + id, students);
            LOGGER.info("Getting student by id from database");
            return student;
        }
    }

    public String createStudent(List<Long> universityIds, Student student) {
        Student newStudent = new Student();
        newStudent.setName(student.getName());
        newStudent.setSurname(student.getSurname());
        newStudent.setFaculty(student.getFaculty());
        newStudent.setSpecialization(student.getSpecialization());
        newStudent.setNumber(student.getNumber());

        if (universityIds == null || universityIds.isEmpty()) {
            newStudent.setUniversities(new ArrayList<>());
            studentRepository.save(newStudent);
        } else {
            newStudent.setUniversities(universityRepository.findAllById(universityIds));
            studentRepository.save(newStudent);
            for (University university : newStudent.getUniversities()) {
                university.getStudents().add(newStudent);
                universityRepository.save(university);
            }
        }
        RequestCache.clear();
        LOGGER.info("Cache cleared in function createStudent");
        return "Successfully created!";
    }

    public String addUniversityToStudent(Long studentId, Long universityId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        University university = universityRepository.findById(universityId).orElse(null);
        if (student != null && university != null && !student.getUniversities().contains(university)) {
            student.getUniversities().add(university);
            university.getStudents().add(student);
            studentRepository.save(student);
            universityRepository.save(university);

            RequestCache.clear();
            LOGGER.info("Cache cleared in function addUniversityToStudent");

            return "Successfully added!";
        } else return "Wrong id(it must have been university id or student id)!";
    }

    public String updateStudent(Long id, Student student) {
        Student newStudent = studentRepository.findById(id).orElse(null);

        if (newStudent == null) {
            return "Not found.";
        } else {
            newStudent.setName(student.getName());
            newStudent.setSurname(student.getSurname());
            newStudent.setFaculty(student.getFaculty());
            newStudent.setSpecialization(student.getSpecialization());
            newStudent.setNumber(student.getNumber());
            studentRepository.save(newStudent);

            RequestCache.clear();
            LOGGER.info("Cache cleared in function updateStudent");

            return "Successfully updated!";
        }
    }

    public String deleteUniversityFromStudent(Long studentId, Long universityId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        University university = universityRepository.findById(universityId).orElse(null);
        if (student != null && university != null && student.getUniversities().contains(university) && university.getStudents().contains(student)) {
            student.getUniversities().remove(university);
            university.getStudents().remove(student);
            studentRepository.save(student);
            universityRepository.save(university);

            RequestCache.clear();
            LOGGER.info("Cache cleared in function deleteUniversityFromStudent");

            return "Successfully deleted!";
        } else return "Wrong id (it must have been university id or student id)!";
    }

    public String deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElse(null);

        if (student != null) {
            List<University> universities = student.getUniversities();

            for (University university : universities) {
                university.getStudents().remove(student);
            }
            studentRepository.delete(student);

            RequestCache.clear();
            LOGGER.info("Cache cleared in function deleteStudent");

            return "Successfully deleted!";
        } else {
            return "Student not found.";
        }
    }
}