package com.example.demo.controller;

import com.example.demo.service.KafkaService;
import com.example.demo.service.TestService;
import com.example.demo.service.impl.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by etomhua on 10/30/2017.
 */
@RestController
public class GreetingController {

    @Autowired
    private RedisServiceImpl redisService;

    @Autowired
    private TestService testService;

    @Autowired
    private KafkaService kafkaService;

    @RequestMapping("/redis")
    public String hello() {
        Map<String, String> map = redisService.sayHello();
        return map.toString();
    }

    @RequestMapping("/test")
    public String test() {
        return testService.doTest();
    }

    @RequestMapping("/kafka")
    public void sendKafka() {
        kafkaService.sayThanks();
    }

}
