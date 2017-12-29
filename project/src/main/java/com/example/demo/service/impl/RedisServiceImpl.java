package com.example.demo.service.impl;

import com.example.demo.service.RedisService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by etomhua on 10/30/2017.
 */
@Service
public class RedisServiceImpl implements RedisService {
    private StringRedisTemplate template;

    public RedisServiceImpl(StringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public Map<String, String> sayHello() {
        Map<String, String> keyPairs = new HashMap<String, String>();
        Set<String> keys = new HashSet<>();/*template.keys("*")*/;
        keys.add("rpk_ep00251");
        keys.add("rpk_ep00tom");
        for(String key : keys) {
            ValueOperations<String, String> operation = template.opsForValue();
            String value = operation.get(key);
            keyPairs.put(key, value);
        }
        return keyPairs;
    }
}
