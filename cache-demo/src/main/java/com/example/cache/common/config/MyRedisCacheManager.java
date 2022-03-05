package com.example.cache.common.config;

import com.example.cache.common.service.itf.CacheSupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;

public class MyRedisCacheManager extends RedisCacheManager {

    public MyRedisCacheManager(RedisOperations redisOperations, CacheSupport cacheSupport) {
        super(redisOperations);
        this.cacheSupport = cacheSupport;
    }

    private static final String SEPARATOR = "#";

    private final CacheSupport cacheSupport;

    @Override
    public Cache getCache(String name) {
        String[] cacheParams = name.split(SEPARATOR);
        String cacheName = cacheParams[0];
        if (StringUtils.isBlank(cacheName)) {
            return null;
        }
        long expirationSeconds = this.computeExpiration(cacheName);
        if (cacheParams.length > 1) {
            expirationSeconds = Long.parseLong(cacheParams[1]);
            this.setDefaultExpiration(expirationSeconds);
        }
        long preloadSeconds = 0;
        boolean isRefresh = false;
        if (cacheParams.length > 2) {
            preloadSeconds = Long.parseLong(cacheParams[2]);
            isRefresh = true;
        }
        MyRedisCache redisCache = new MyRedisCache(
                cacheName,
                this.isUsePrefix()?this.getCachePrefix().prefix(cacheName):null,
                this.getRedisOperations(),
                expirationSeconds,
                preloadSeconds,
                isRefresh,
                cacheSupport
        );
        return redisCache;
    }
}
