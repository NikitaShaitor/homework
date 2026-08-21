package ru.otus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SharedState sharedState = new SharedState();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable task1 = new NumberPrinterTask(1, sharedState);
        Runnable task2 = new NumberPrinterTask(2, sharedState);

        executor.execute(task1);
        executor.execute(task2);
        executor.shutdown();
        executor.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);
    }
}
