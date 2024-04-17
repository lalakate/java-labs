package com.kate.lab1.exception;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandlerController {
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ExceptionDetails> notFound(NotFoundException exception, WebRequest request) {
    ExceptionDetails details = new ExceptionDetails(new Date(), exception.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ExceptionDetails> badRequest(BadRequestException exception, WebRequest request) {
    ExceptionDetails details = new ExceptionDetails(new Date(), exception.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionDetails> internalServiceException(Exception exception, WebRequest request) {
    ExceptionDetails details = new ExceptionDetails(new Date(), exception.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
