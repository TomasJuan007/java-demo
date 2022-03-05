package com.example.cache.common.config;

import com.example.cache.common.service.itf.CacheSupport;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {

    private Logger LOGGER = LoggerFactory.getLogger(RedisCacheConfig.class);

    @Autowired
    private CacheSupport cacheSupport;

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        MyRedisCacheManager redisCacheManager = new MyRedisCacheManager(redisTemplate, cacheSupport);
        redisCacheManager.setDefaultExpiration(3600L);
        redisCacheManager.setTransactionAware(true);
        redisCacheManager.setUsePrefix(true);
        redisCacheManager.setCachePrefix(new DefaultRedisCachePrefix(":"));
        redisCacheManager.afterPropertiesSet();
        Map<String, Long> expiresMap = new HashMap<>();
        redisCacheManager.setExpires(expiresMap);
        return redisCacheManager;
    }

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuffer sb = new StringBuffer();
            sb.append(target.getClass().getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            long hashCode = Hashing.murmur3_128().hashString(sb, Charset.defaultCharset()).asLong();
            return new StringBuilder(method.getName()).append(":").append(hashCode).toString();
        };
    }

    @Override
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {
                redisErrorException(e, o);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
                redisErrorException(e, o);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
                redisErrorException(e, o);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                redisErrorException(e, null);
            }
        };
        return cacheErrorHandler;
    }

    private void redisErrorException(RuntimeException e, Object o) {
        LOGGER.error("redis-cache error: key=[{}], exception={}", o, e.getMessage(), e);
    }
}
