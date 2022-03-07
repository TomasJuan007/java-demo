package com.example.cache.common.cache.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisLock {
    private static Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);

    private RedisTemplate redisTemplate;

    private static final int DEFAULT_ACQUIRE_RESOLUTION_MILLIS = 100;

    private String lockKey;

    private int expireMs = 60*1000;

    private int timeoutMs = 10*1000;

    private volatile boolean locked = false;;

    public RedisLock(RedisTemplate redisTemplate, String lockKey) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey + "_lock";
    }

    public RedisLock(RedisTemplate redisTemplate, String lockKey, int timeoutMs) {
        this(redisTemplate, lockKey);
        this.timeoutMs = timeoutMs;
    }

    public RedisLock(RedisTemplate redisTemplate, String lockKey, int timeoutMs, int expireMs) {
        this(redisTemplate, lockKey, timeoutMs);
        this.expireMs = expireMs;
    }

    public String getLockKey() {
        return lockKey;
    }

    private String get(final String key) {
        Object obj = null;
        try {
            obj = redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] data = redisConnection.get(serializer.serialize(key));
                    redisConnection.close();
                    if (data == null) {
                        return null;
                    }
                    return serializer.deserialize(data);
                }
            });
        } catch (Exception e) {
            LOGGER.error("get redis error, key:{}", key);
        }
        return obj != null ? obj.toString() : null;
    }

    private boolean setNX(final String key, final String value) {
        Object obj = null;
        try {
            obj = redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    Boolean success = redisConnection.setNX(serializer.serialize(key), serializer.serialize(value));
                    redisConnection.close();
                    return success;
                }
            });
        } catch (Exception e) {
            LOGGER.error("set nx redis error, key:{}", key);
        }
        return obj != null ? (Boolean) obj : false;
    }

    private String getSet(final String key, final String value) {
        Object obj = null;
        try {
            obj = redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] ret = redisConnection.getSet(serializer.serialize(key), serializer.serialize(value));
                    redisConnection.close();
                    return serializer.deserialize(ret);
                }
            });
        } catch (Exception e) {
            LOGGER.error("get set redis error, key:{}", key);
        }
        return obj != null ? (String) obj : null;
    }

    public synchronized boolean lock() throws InterruptedException {
        int timeout = timeoutMs;
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMs + 1;
            String expiresStr = String.valueOf(expires);
            if (this.setNX(lockKey, expiresStr)) {
                locked = true;
                return true;
            }

            String currentValueStr = this.get(lockKey);
            if (currentValueStr!=null && Long.parseLong(currentValueStr)<System.currentTimeMillis()) {
                String oldValueStr = this.getSet(lockKey, expiresStr);
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    locked = true;
                    return true;
                }
            }
            timeout -= DEFAULT_ACQUIRE_RESOLUTION_MILLIS;

            this.wait(DEFAULT_ACQUIRE_RESOLUTION_MILLIS);
            }
        return false;
    }

    public synchronized boolean easyLock() throws InterruptedException {
        long expires = System.currentTimeMillis() + expireMs + 1;
        String expireStr = String.valueOf(expires);
        if (this.setNX(lockKey, expireStr)) {
            this.locked = true;
            return true;
        }
        return false;
    }

    public synchronized void unlock() {
        if (locked) {
            redisTemplate.delete(lockKey);
            locked = false;
        }
    }
}
