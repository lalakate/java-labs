package com.kate.lab1.service;

import com.kate.lab1.model.Student;
import com.kate.lab1.model.University;
import com.kate.lab1.repository.StudentRepository;
import com.kate.lab1.repository.UniversityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {
    private StudentRepository studentRepository;
    private UniversityRepository universityRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
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

            return "Successfully deleted!";
        } else {
            return "Student not found.";
        }
    }
}