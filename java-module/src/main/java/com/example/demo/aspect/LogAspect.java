package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(1)
@Component
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.example.demo.controller.*.* (..))")
    public void apiLogAspect() {}

    @Before("apiLogAspect()")
    public void doBefore(JoinPoint joinPoint) {}

    @AfterReturning(returning = "ret", pointcut = "apiLogAspect()")
    public void doAfterReturning(Object ret) {
        logger.info("RESPONSE: " + ret);
    }
}
