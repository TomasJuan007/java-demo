package com.example.cache.common.invoke.aspect;

import com.example.cache.common.invoke.annotation.MyCacheMethodInvoker;
import com.example.cache.common.invoke.invocation.InvocationRegistry;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(2)
public class MyCacheMethodInvokerAspect {
    @Autowired
    private InvocationRegistry cacheRefreshSupport;

    @Before("@annotation(com.example.cache.common.invoke.annotation.MyCacheMethodInvoker)")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MyCacheMethodInvoker myCacheMethodInvoker = method.getAnnotation(MyCacheMethodInvoker.class);
        cacheRefreshSupport.registerInvocation(joinPoint.getTarget(), method, joinPoint.getArgs(), myCacheMethodInvoker.value());
    }
}
