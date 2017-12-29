package com.example.demo.service.impl;

import com.example.demo.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    @Override
    public void sayThanks() {
        kafkaTemplate.send("1","hello,thanku,thanku very much");
    }
}
