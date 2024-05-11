package com.kate.lab1.aop;

import com.kate.lab1.service.RequestCounterService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestCounterAspect {
  private final RequestCounterService requestCounterService;

  public RequestCounterAspect(RequestCounterService requestCounterService) {
    this.requestCounterService = requestCounterService;
  }

  @Around(
      "@within(com.kate.lab1.aop.RequestStats) ||" + "@annotation(com.kate.lab1.aop.RequestStats)")
  public Object incrementRequestStats(ProceedingJoinPoint joinPoint) throws Throwable {
    requestCounterService.incrementRequestCount();

    return joinPoint.proceed();
  }
}