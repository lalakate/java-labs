package com.kate.lab1.exception;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class ExceptionDetails {
  private Date timestamp;
  private String message;
  private String details;
}
