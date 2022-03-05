package com.example.cache.common.config;

import com.example.cache.common.service.itf.CacheSupport;
import com.example.cache.common.vo.CachedInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.core.RedisOperations;

import java.util.concurrent.TimeUnit;

/**
 * 1.增加一级缓存，减少使用redis缓存
 * 2.增加缓存预刷新，防止缓存击穿或雪崩造成并发请求数据库
 * 3.数据库取数加锁，防止并发
 */
public class MyRedisCache extends RedisCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyRedisCache.class);

    private final byte[] prefix;
    private final RedisOperations redisOperations;
    private long expireSecond;
    private long preloadSecond;
    private final CacheSupport cacheSupport;
    private boolean isRefresh;

    public MyRedisCache(String name, byte[] prefix, RedisOperations<?, ?> redisOperations, long expirationSecond, long preloadSecond, boolean isRefresh, CacheSupport cacheSupport) {
        super(name, prefix, redisOperations, expirationSecond);
        this.prefix = prefix;
        this.redisOperations = redisOperations;
        this.expireSecond = expirationSecond;
        this.preloadSecond = preloadSecond;
        this.cacheSupport = cacheSupport;
        this.isRefresh = isRefresh;
    }

    @Override
    public ValueWrapper get(Object key) {
        RedisCacheKey redisCacheKey = getRedisCacheKey(key);
        ValueWrapper valueWrapper = this.get(redisCacheKey);
        try {
            return getValueWrapper(valueWrapper, key, redisCacheKey);
        } catch (Exception e) {
            LOGGER.error("MyRedisCache#get error, key:{},cacheKey:{}", key, new String(redisCacheKey.getKeyBytes()));
            return valueWrapper;
        }
    }

    private RedisCacheKey getRedisCacheKey(Object key) {
        RedisCacheKey redisCacheKey = new RedisCacheKey(key).usePrefix(this.prefix)
                .withKeySerializer(this.redisOperations.getKeySerializer());
        return redisCacheKey;
    }

    private ValueWrapper getValueWrapper(ValueWrapper valueWrapper, Object key, RedisCacheKey redisCacheKey) {
        if (valueWrapper!=null) {
            Long ttl = this.redisOperations.getExpire(getCacheKey(key));
            if (this.isRefresh && ttl!=null && ttl<preloadSecond) {
                ValueWrapper vw = getDataLock(key,redisCacheKey);
                if (vw!=null) {
                    return vw;
                }
            }
            return valueWrapper;
        } else {
            if (!this.isRefresh) {
                return valueWrapper;
            }

            ValueWrapper vw = getDataLock(key,redisCacheKey);
            if (vw!=null) {
                return vw;
            } else {
                int i = 0;
                while (vw==null) {
                    try {
                        Thread.sleep(200L);
                        vw = this.get(redisCacheKey);
                    } catch (Exception e) {
                        LOGGER.error("MyRedisCache#getValueWrapper error, key:{}, cacheKey:{}", key, new String(redisCacheKey.getKeyBytes()));
                    }
                    i++;
                    if (i >= 5) {
                        break;
                    }
                }
                return vw;
            }
        }
    }

    private ValueWrapper getDataLock(Object key, RedisCacheKey redisCacheKey) {
        //TODO: redis lock
        try {
            refreshCache(key.toString());
            return this.get(redisCacheKey);
        } catch (Exception e) {
            LOGGER.error("MyRedisCache#getDataLock refresh error, key:{}, cacheKey:{}", key, new String(redisCacheKey.getKeyBytes()));
        }
        return null;
    }

    private void refreshCache(String key) {
        CachedInvocation cachedInvocation = cacheSupport.getCachedInvocationByKey(super.getName(), key);
        if (cachedInvocation!=null) {
            try {
                Object invoke = cacheSupport.refreshCache(cachedInvocation);
                this.put(key, invoke);
                this.redisOperations.expire(key, expireSecond, TimeUnit.SECONDS);
            } catch (Exception e) {
                LOGGER.error("MyRedisCache#refreshCache error, key:{}, cacheKey:{}", key, getCacheKey(key));
            }
        }
    }

    private String getCacheKey(Object key) {
        RedisCacheKey redisCacheKey = getRedisCacheKey(key);
        return new String(redisCacheKey.getKeyBytes());
    }
}
