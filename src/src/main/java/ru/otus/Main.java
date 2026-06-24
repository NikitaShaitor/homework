package ru.otus;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JsonProcessor processor = new JsonProcessor();
        try {
            processor.process("input.json", "output.json");
            System.out.println("\nПрограмма завершена успешно!");
        } catch (IOException e) {
            System.err.println("Произошла ошибка при обработке файла:");
            e.printStackTrace();
        }
    }
}