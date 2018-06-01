package com.example.demo.service.consumer;

public interface Model {
    Runnable newRunnableConsumer();
    Runnable newRunnableProducer();
}
