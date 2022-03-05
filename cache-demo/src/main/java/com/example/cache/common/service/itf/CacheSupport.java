package com.example.cache.common.service.itf;

import com.example.cache.common.vo.CachedInvocation;

public interface CacheSupport {

    CachedInvocation getCachedInvocationByKey(String cacheName, String cacheKey);

    Object refreshCache(CachedInvocation invocation) throws Exception;
}
