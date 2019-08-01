package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by etomhua on 10/30/2017.
 */
@Component
//@PropertySource("classpath:application-kafka.properties")
@ConfigurationProperties(prefix = "kafka")
public class KafkaPropertiesHolder {
    private Properties config = new Properties();

    public Properties getConfig() {
        return config;
    }
}
