package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Configuration
public class ScheduleConfiguration {
    private boolean flag;

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void run() throws ExecutionException, InterruptedException {
        Callable<String> callableTask = () -> {
            TimeUnit.MILLISECONDS.sleep(3000);
            if (flag=!flag) {
                throw new Exception();
            }
            return "Task1's execution";
        };
        Callable<String> callableTask2 = () -> {
            TimeUnit.MILLISECONDS.sleep(3000);
            return "Task2's execution";
        };
        List<Callable<String>> callableTasks = new ArrayList<>();
        callableTasks.add(callableTask);
        callableTasks.add(callableTask2);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        String result = executorService.invokeAny(callableTasks);
        System.out.println(new Date() + " " + result);

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
