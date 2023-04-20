package com.example.cache.common.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cache.common")
@Data
public class CacheProperties {
    private boolean ehcacheCloneSwitch;
    private String cloneType;
}
