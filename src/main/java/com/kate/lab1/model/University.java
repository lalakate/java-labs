package com.kate.lab1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "universities")
@Data
public class University {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "domain")
  private String domain;

  @Column(name = "index")
  private String index;

  @Column(name = "web_page")
  @JsonProperty("web_page")
  private String webPage;

  @ManyToOne
  @JoinColumn(name = "country_id")
  @JsonIgnoreProperties(value = {"universities", "country", "students"})
  private Country country;

  @ManyToMany
  @JoinTable(name = "university_student", joinColumns = @JoinColumn(name = "university_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
  @JsonIgnoreProperties(value = {"universities", "country", "students"})
  private List<Student> students;
}