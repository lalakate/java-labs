package com.kate.lab1.service;

import com.kate.lab1.cache.RequestCache;
import com.kate.lab1.model.Country;
import com.kate.lab1.model.Student;
import com.kate.lab1.model.University;
import com.kate.lab1.repository.CountryRepository;
import com.kate.lab1.repository.StudentRepository;
import com.kate.lab1.repository.UniversityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class UniversityService {
    private static final String ALL_UNIVERSITIES_REQUEST = "http://localhost:8080/api/v1/university/all";
    private static final String UNIVERSITY_BY_ID_REQUEST = "http://localhost:8080/api/v1/university/";

    private UniversityRepository universityRepository;
    private CountryRepository countryRepository;
    private StudentRepository studentRepository;

    private static final Logger LOGGER = Logger.getLogger(UniversityService.class.getName());

    public List<University> getAllUniversities() {
        if(RequestCache.containsKey(ALL_UNIVERSITIES_REQUEST)) {
            LOGGER.info("Getting all universities from cache");
            return (List<University>) RequestCache.get(ALL_UNIVERSITIES_REQUEST);
        }
        LOGGER.info("Getting data from database");
        List<University> universities = universityRepository.findAll();
        RequestCache.put(ALL_UNIVERSITIES_REQUEST, universities);
        return universities;
    }

    public University getUniversityById(Long id) {
        if(RequestCache.containsKey(UNIVERSITY_BY_ID_REQUEST + id)) {
            LOGGER.info("Getting university by id from cache");
            return ((List<University>)RequestCache.get(UNIVERSITY_BY_ID_REQUEST + id)).get(0);
        }
        else {
            University university = universityRepository.findById(id).orElse(null);
            List<University> universities = new ArrayList<>();
            universities.add(university);
            RequestCache.put(UNIVERSITY_BY_ID_REQUEST + id, universities);
            LOGGER.info("Getting university by id from database");
            return university;
        }
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

            RequestCache.clear();
            LOGGER.info("Cache cleared in function createUniversity");

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

            RequestCache.clear();
            LOGGER.info("Cache cleared in function updateUniversity");

            return "Successfully updated!";
        }
    }

    public String deleteUniversity(Long id) {
        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (optionalUniversity.isPresent()) {
            universityRepository.deleteById(id);

            RequestCache.clear();
            LOGGER.info("Cache cleared in function deleteUniversity");

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

            RequestCache.clear();
            LOGGER.info("University cache cleared in function addStudentToUniversity");

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

            RequestCache.clear();
            LOGGER.info("University cache cleared in function deleteStudentFromUniversity");

            return "Successfully deleted!";
        } else return "Wrong id or this university does not contain this student.";
    }
}