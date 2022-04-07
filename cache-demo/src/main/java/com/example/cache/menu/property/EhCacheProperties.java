package com.example.cache.menu.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cache.menu")
@Data
public class EhCacheProperties {
    private boolean ehcacheCloneSwitch;
}
