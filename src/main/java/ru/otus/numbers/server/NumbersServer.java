package ru.otus.numbers.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(scanBasePackages = {"ru.otus.numbers"})
@Slf4j
public class NumbersServer implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NumbersServer.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("gRPC Server is starting on port 6565...");
    }
}