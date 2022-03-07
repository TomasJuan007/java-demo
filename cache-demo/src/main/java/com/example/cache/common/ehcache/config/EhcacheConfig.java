package com.example.cache.common.ehcache.config;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/**
 * 增加一级缓存，减少使用redis缓存
 */
@Configuration
public class EhcacheConfig {
    private static String MENU_LIST_INFO = "menuListInfo";
    @Bean
    public Cache<String, List> menuListInfoEhcache() {
        ResourcePoolsBuilder cacheSize = ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(1, EntryUnit.ENTRIES);
        CacheConfigurationBuilder<String,List> cacheConfigurationBuilder = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, List.class, cacheSize)
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(3)));
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(MENU_LIST_INFO, cacheConfigurationBuilder)
                .build(true);
        Cache<String, List> cache = cacheManager.getCache(MENU_LIST_INFO, String.class, List.class);
        return cache;
    }
}
