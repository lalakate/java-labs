package com.kate.lab1.service;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class RequestCounterService {
  private final AtomicInteger requestCount = new AtomicInteger(0);

  public void incrementRequestCount() {
    requestCount.incrementAndGet();
  }

  public int getRequestCount() {
    return requestCount.get();
  }
}