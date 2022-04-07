package com.example.cache.common.ehcache.aspect;

import com.example.cache.common.ehcache.annotation.MyEhCache;
import com.example.cache.common.property.CacheProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationUtils;
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

import java.io.Serializable;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)
public class MyEhCacheAspect {
    private Logger logger = LoggerFactory.getLogger(MyEhCache.class);

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private CacheProperties cacheProperties;

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
        } else {
            result = joinPoint.proceed();
            beanEhcache.put(cacheNames[1], result);
        }

        //克隆开关
        if (!cacheProperties.isEhcacheCloneSwitch()) {
            logger.info("MyEhCacheAspect#doAround return result directly.");
            return result;
        }

        Object res;
        if (cacheProperties.isUseNewClone()) {
            //新的克隆方式，要求对象及其引用对象对应的类实现Serializable接口
            try {
                res = SerializationUtils.clone((Serializable) result);
                logger.info("MyEhCacheAspect#doAround new cloned.");
            } catch (Exception e) {
                logger.error("MyEhCacheAspect#doAround exception", e);
                return result;
            }
        } else {
            //旧的克隆方式，压测发现存在性能问题
            String json;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
                json = objectMapper.writeValueAsString(result);
                res = objectMapper.readValue(json, Object.class);
                logger.info("MyEhCacheAspect#doAround old cloned.");
            } catch (JsonProcessingException e) {
                logger.error("MyEhCacheAspect#doAround JsonProcessingException", e);
                return result;
            }
        }

        return res;

    }
}
