package com.kate.lab1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

@Table(name = "students")
@Entity
@Data
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @Column(name = "faculty")
  private String faculty;

  @Column(name = "specialization")
  private String specialization;

  @Column(name = "number")
  private Long number;

  @ManyToMany(mappedBy = "students")
  @JsonIgnoreProperties(value = {"universities", "country", "students"})
  private List<University> universities;
}