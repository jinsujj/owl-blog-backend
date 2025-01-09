package me.blog.backend.common.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
  private final ObjectMapper objectMapper;

  public LoggingAspect() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  // api layer logging
  @Pointcut("execution(* me.blog.backend.api..*(..))")
  public void controllerMethods(){}


  @Around("controllerMethods()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    // Method name and Class info
    log.info("Start Method: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

    // Arguments info
    Object[] args = joinPoint.getArgs();
    if(args.length > 0){
      StringBuilder params = new StringBuilder("Arguments: \n");
      for(int i =0; i < args.length; i++){
        params.append(" [").append(i).append("]: ")
            .append(args[i] != null ? toJson(args[i]) : "null")
            .append("\n");
      }
      log.info(params.toString());
    } else{
      log.info("No arguments provided");
    }

    Object result = null;
    try{
      result = joinPoint.proceed();
    }
    catch (Exception ex){
      setErrorLog(joinPoint, ex);
      throw ex;
    }

    long elapsedTime = System.currentTimeMillis() - start;

    log.info("End Method: {}.{} [execution time={} ms] result={}",
        joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(),
        elapsedTime,
        toJson(result));

    return result;
  }

  @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
  public void logException(JoinPoint joinPoint, Throwable exception) {
    setErrorLog(joinPoint,exception);
  }

  private void setErrorLog(JoinPoint joinPoint, Throwable ex) {
    log.error("Exception in {}.{} with cause='{}'",
        joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(),
        ex.getCause() != null ? ex.getCause() : "NULL");
  }

  public String toJson(Object obj){
    try{
      return objectMapper.writeValueAsString(obj);
    }
    catch (Exception ex){
      return "Error converting object to JSON" + ex.getMessage();
    }
  }
}
