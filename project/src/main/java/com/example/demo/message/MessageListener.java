package com.example.demo.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

/**
 * Created by etomhua on 11/3/2017.
 */
@Component
public class MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @KafkaListener(topics = "1", group = "topic1",
                  containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(String message) {
        logger.info(message);
    }
}
