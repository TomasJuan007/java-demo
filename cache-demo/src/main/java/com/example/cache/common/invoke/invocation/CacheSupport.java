package com.example.cache.common.invoke.invocation;

public interface CacheSupport {

    CachedInvocation getCachedInvocationByKey(String cacheName, String cacheKey);

    Object refreshCache(CachedInvocation invocation) throws Exception;
}
