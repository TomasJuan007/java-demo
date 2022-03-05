package com.example.cache.common.aspect;

import com.example.cache.common.annotation.MyRefreshCacheKey;
import com.example.cache.common.service.itf.InvocationRegistry;
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

    @Before("@annotation(com.example.cache.common.annotation.MyRefreshCacheKey)")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MyRefreshCacheKey myRefreshCacheKey = method.getAnnotation(MyRefreshCacheKey.class);
        cacheRefreshSupport.registerInvocation(joinPoint.getTarget(), method, joinPoint.getArgs(), myRefreshCacheKey.value());
    }
}
