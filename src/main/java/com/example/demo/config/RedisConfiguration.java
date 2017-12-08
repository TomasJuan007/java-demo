package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by etomhua on 10/30/2017.
 */
@Configuration
public class RedisConfiguration {
    @Bean
    JedisConnectionFactory getFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName("10.175.172.251");
        factory.setPort(6379);
        return factory;
    }

    @Bean
    StringRedisTemplate template() {
        RedisConnectionFactory connectionFactory = getFactory();
        return new StringRedisTemplate(connectionFactory);
    }
}
