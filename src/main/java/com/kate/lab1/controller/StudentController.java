package com.kate.lab1.controller;

import com.kate.lab1.model.Student;
import com.kate.lab1.service.StudentService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/student")
public class StudentController {
  private final StudentService studentService;

  @GetMapping("/all")
  public List<Student> getAllStudents() {
    return studentService.getAllStudents();
  }

  @GetMapping("/{id}")
  public Student getStudentById(@PathVariable Long id) {
    return studentService.getStudentById(id);
  }

  @PostMapping("/create")
  public String createStudent(@RequestParam(required = false) List<Long> universityIds,
                              @RequestBody Student student) {
    return studentService.createStudent(universityIds, student);
  }

  @PatchMapping("/update/{id}")
  public String updateStudent(@PathVariable Long id, @RequestBody Student student) {
    return studentService.updateStudent(id, student);
  }

  @PatchMapping("/addUniversityToStudent/{id}")
  public String addUniversityToStudent(@PathVariable Long id, @RequestParam Long universityId) {
    return studentService.addUniversityToStudent(id, universityId);
  }

  @PatchMapping("/deleteUniversityFromStudent/{id}")
  public String deleteUniversityFromStudent(@PathVariable Long id,
                                            @RequestParam Long universityId) {
    return studentService.deleteUniversityFromStudent(id, universityId);
  }

  @DeleteMapping("/delete/{id}")
  public String deleteStudent(@PathVariable Long id) {
    return studentService.deleteStudent(id);
  }
}