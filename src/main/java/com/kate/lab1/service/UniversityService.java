package com.kate.lab1.service;

import com.kate.lab1.model.Country;
import com.kate.lab1.model.Student;
import com.kate.lab1.model.University;
import com.kate.lab1.repository.CountryRepository;
import com.kate.lab1.repository.StudentRepository;
import com.kate.lab1.repository.UniversityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UniversityService {
    private UniversityRepository universityRepository;
    private CountryRepository countryRepository;
    private StudentRepository studentRepository;

    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    public University getUniversityById(Long id) {
        return universityRepository.findById(id).orElse(null);
    }

    public String createUniversity(Long countryId, University university) {
        Set<Student> uniqueStudents = new HashSet<>();
        for (Student student : university.getStudents()) {
            if (!uniqueStudents.add(student))
                return "Bad request, do not share the same students in the same university";
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
            return "Wrong id, operation failed!";
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

            return "Successfully created!";
        }
    }

    public String updateUniversity(Long id, University university) {
        University newUniversity = universityRepository.findById(id).orElse(null);

        if (newUniversity == null) {
            return "Not found.";
        } else {
            newUniversity.setName(university.getName());
            newUniversity.setDomain(university.getDomain());
            newUniversity.setIndex(university.getIndex());
            newUniversity.setWebPage(university.getWebPage());
            newUniversity.setCountry(university.getCountry());
            newUniversity.setStudents(university.getStudents());
            universityRepository.save(newUniversity);
            return "Successfully updated!";
        }
    }

    public String deleteUniversity(Long id) {
        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (optionalUniversity.isPresent()) {
            universityRepository.deleteById(id);
            return "Successfully deleted!";
        } else {
            return "University with id " + id + " not found";
        }
    }

    public String addStudentToUniversity(Long universityId, Long studentId) {
        University university = universityRepository.findById(universityId).orElse(null);
        Student student = studentRepository.findById(studentId).orElse(null);

        if (university != null && student != null && !university.getStudents().contains(student)) {
            student.getUniversities().add(university);
            university.getStudents().add(student);
            studentRepository.save(student);
            universityRepository.save(university);

            return "Successfully added!";
        } else return "Wrong id or this student exists in university.";
    }

    public String deleteStudentFromUniversity(Long universityId, Long studentId) {
        University university = universityRepository.findById(universityId).orElse(null);
        Student student = studentRepository.findById(studentId).orElse(null);

        if (university != null && student != null && university.getStudents().contains(student) && student.getUniversities().contains(university)) {
            student.getUniversities().remove(university);
            university.getStudents().remove(student);
            studentRepository.save(student);
            universityRepository.save(university);

            return "Successfully deleted!";
        } else return "Wrong id or this university does not contain this student.";
    }
}