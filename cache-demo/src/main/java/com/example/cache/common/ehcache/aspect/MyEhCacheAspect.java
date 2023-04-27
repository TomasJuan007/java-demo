package com.example.cache.common.ehcache.aspect;

import com.example.cache.common.ehcache.annotation.MyEhCache;
import com.example.cache.common.property.CacheProperties;
import com.example.cache.common.utils.KryoUtils;
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
import org.springframework.util.StopWatch;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

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
        StopWatch stopWatch = new StopWatch("deep-copy");
        switch (cacheProperties.getCloneType()) {
            case "json"://Jackson序列化克隆方式，压测发现存在性能问题
                stopWatch.start("Jackson");
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
                stopWatch.stop();
                break;
            case "binary"://Kryo序列化克隆方式，动态生成字节码方式解决性能问题
                stopWatch.start("Kryo");
                Type type = method.getGenericReturnType();
                res = KryoUtils.copy(result, type);
                stopWatch.stop();
                break;
            case "clone"://Java序列化克隆方式，要求对象及其引用对象对应的类实现Serializable接口
            default:
                stopWatch.start("SerializationUtils");
                try {
                    res = SerializationUtils.clone((Serializable) result);
                    logger.info("MyEhCacheAspect#doAround new cloned.");
                } catch (Exception e) {
                    logger.error("MyEhCacheAspect#doAround exception", e);
                    return result;
                }
                stopWatch.stop();
        }

        for (StopWatch.TaskInfo taskInfo : stopWatch.getTaskInfo()) {
            logger.info("MyEhCacheAspect#doAround " + taskInfo.getTaskName() + " clone consume(ms):" + taskInfo.getTimeMillis());
        }

        return res;

    }
}
