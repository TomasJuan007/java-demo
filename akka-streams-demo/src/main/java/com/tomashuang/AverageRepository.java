package com.tomashuang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AverageRepository {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public CompletionStage<Double> save(Double average) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("saving average: {}", average);
            return average;
        });
    }
}
