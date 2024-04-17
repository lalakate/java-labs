package com.kate.lab1.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
  @Pointcut("@annotation(com.kate.lab1.aspect.Logging)")
  private void serviceLogging() {
  }

  @Around("serviceLogging()")
  public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    log.info("Called method {}() with args: {}", methodName, args);
    try {
      Object output = joinPoint.proceed();
      log.info("Method {} is returned with value: {}", methodName, output);
      return output;
    } catch (Throwable exception) {
      log.error(
          "In the method {}() throwed exception with message: {}",
          methodName,
          exception.getMessage());
      throw exception;
    }
  }
}
