package com.example.cache.common.service.impl;

import com.example.cache.common.service.itf.CacheSupport;
import com.example.cache.common.service.itf.InvocationRegistry;
import com.example.cache.common.vo.CachedInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class CacheSupportImpl implements CacheSupport, InvocationRegistry {

    private Map<String, Set<CachedInvocation>> cachedInvocationsMap = new ConcurrentHashMap<>();

    @Override
    public CachedInvocation getCachedInvocationByKey(String cacheName, String cacheKey) {
        if (cachedInvocationsMap.get(cacheName)!=null) {
            for (final CachedInvocation invocation : cachedInvocationsMap.get(cacheName)) {
                if (StringUtils.isNotBlank(cacheKey) && invocation.getKey().toString().equals(cacheKey)) {
                    return invocation;
                }
            }
        }
        return null;
    }

    @Override
    public Object refreshCache(CachedInvocation invocation) throws Exception {
        final MethodInvoker invoker = new MethodInvoker();
        invoker.setTargetObject(invocation.getTargetBean());
        invoker.setStaticMethod(invocation.getTargetMethod().getName());
        invoker.setArguments(invoker.getArguments());
        invoker.prepare();
        return invoker.invoke();
    }

    @Override
    public void registerInvocation(Object invokeBean, Method invokeMethod, Object[] invocationArguments, String cacheName) {
        String[] cacheParams = cacheName.split(":");
        final CachedInvocation invocation = new CachedInvocation(
                cacheParams[1], invokeBean, invokeMethod, invocationArguments);
        String realCacheName = cacheParams[0];
        if (!cachedInvocationsMap.containsKey(realCacheName)) {
            cachedInvocationsMap.put(realCacheName, new CopyOnWriteArraySet<>());
        }
        cachedInvocationsMap.get(realCacheName).add(invocation);
    }
}
