package com.example.cache.common.cache.data;

import com.example.cache.common.invoke.invocation.CacheSupport;
import com.example.cache.common.cache.lock.RedisLock;
import com.example.cache.common.invoke.invocation.CachedInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

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
            return getValueWrapper(valueWrapper, key);
        } catch (Exception e) {
            LOGGER.error("MyRedisCache#get error, key:{},cacheKey:{}", key, new String(redisCacheKey.getKeyBytes()));
            return valueWrapper;
        }
    }

    private ValueWrapper getValueWrapper(ValueWrapper valueWrapper, Object key) {
        if (!this.isRefresh) {
            return valueWrapper;
        }

        RedisCacheKey redisCacheKey = getRedisCacheKey(key);
        String cacheKeyStr = new String(redisCacheKey.getKeyBytes());

        if (valueWrapper!=null) {
            //如果能获取到数据，检查是否需要预刷新
            Long ttl = this.redisOperations.getExpire(cacheKeyStr);
            if (ttl!=null && ttl<preloadSecond) {
                ValueWrapper vw = refreshAndGet(key);
                if (vw!=null) {
                    return vw;
                }
            }
            return valueWrapper;
        } else {
            //如果获取不到数据，直接刷新，并在失败的情况下做五次重试
            ValueWrapper vw = refreshAndGet(key);
            if (vw == null) {
                int i = 0;
                while (vw == null) {
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
            }
            return vw;
        }
    }

    private ValueWrapper refreshAndGet(Object key) {
        RedisCacheKey redisCacheKey = getRedisCacheKey(key);
        String cacheKeyStr = new String(redisCacheKey.getKeyBytes());
        try {
            RedisLock redisLock = new RedisLock((RedisTemplate) redisOperations, cacheKeyStr);
            if (redisLock.easyLock()) {
                try {
                    refreshCache(key.toString());
                    return this.get(redisCacheKey);
                } catch (Exception e) {
                    LOGGER.error("MyRedisCache#getDataLock refresh error, key:{}, cacheKey:{}", key, cacheKeyStr);
                } finally {
                    redisLock.unlock();
                }
            }
        } catch (Exception e) {
            LOGGER.error("MyRedisCache#getDataLock set lock error, key:{}, cacheKey:{}", key, cacheKeyStr);
        }
        LOGGER.error("MyRedisCache#getDataLock lock failed, key:{}, cacheKey:{}", key, cacheKeyStr);
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
                RedisCacheKey redisCacheKey = getRedisCacheKey(key);
                String cacheKeyStr= new String(redisCacheKey.getKeyBytes());
                LOGGER.error("MyRedisCache#refreshCache error, key:{}, cacheKey:{}", key, cacheKeyStr);
            }
        }
    }

    private RedisCacheKey getRedisCacheKey(Object key) {
        RedisCacheKey redisCacheKey = new RedisCacheKey(key)
                .usePrefix(this.prefix)
                .withKeySerializer(this.redisOperations.getKeySerializer());
        return redisCacheKey;
    }
}
