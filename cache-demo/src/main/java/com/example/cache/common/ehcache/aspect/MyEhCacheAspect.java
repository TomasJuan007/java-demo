package com.example.cache.common.ehcache.aspect;

import com.example.cache.common.ehcache.annotation.MyEhCache;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)
public class MyEhCacheAspect {
    private Logger logger = LoggerFactory.getLogger(MyEhCache.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Around("@annotation(com.example.cache.common.ehcache.annotation.MyEhCache)")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MyEhCache myEhCache = method.getAnnotation(MyEhCache.class);
        String cacheName = myEhCache.value();
        logger.info("MyEhCacheAspect#doAround cacheName:{}", cacheName);
        if (StringUtils.isBlank(cacheName)) {
            return joinPoint.proceed();
        }
        String[] cacheNames = cacheName.split(":");
        Cache beanEhcache = (Cache) applicationContext.getBean(cacheNames[0]);
        Object obj = beanEhcache.get(cacheNames[1]);
        if (obj!=null) {
            return obj;
        }
        result = joinPoint.proceed();
        beanEhcache.put(cacheNames[1], result);
        return result;

    }
}
