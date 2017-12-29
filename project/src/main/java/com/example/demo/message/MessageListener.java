package com.example.demo.message;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by etomhua on 11/3/2017.
 */
@Component
public class MessageListener {
    @KafkaListener(topics = "1", group = "topic1",
                  containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(String message) {
        System.out.println(message);
    }
}
