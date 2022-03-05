package com.example.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan("com.example.cache")
@EnableAspectJAutoProxy
public class CacheSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheSpringBootApplication.class, args);
    }
}
