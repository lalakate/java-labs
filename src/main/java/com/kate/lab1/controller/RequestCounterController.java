package com.kate.lab1.controller;

import com.kate.lab1.service.RequestCounterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class RequestCounterController {
  private RequestCounterService requestCounterService;

  public RequestCounterController(RequestCounterService requestCounterService) {
    this.requestCounterService = requestCounterService;
  }

  @GetMapping
  public ResponseEntity<Integer> getRequestStats() {
    return ResponseEntity.ok(requestCounterService.getRequestCount());
  }
}