package ru.otus.core.repository.executor;

public class DbExecutorException extends RuntimeException {

    public DbExecutorException(String message) {
        super(message);
    }

    public DbExecutorException(Throwable cause) {
        super(cause);
    }
}