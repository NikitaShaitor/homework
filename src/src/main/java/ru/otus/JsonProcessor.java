package ru.otus;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonProcessor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<Integer> TRUSTED_USER_IDS = List.of(101, 205, 312);

    public void process(String inputFilePath, String outputFilePath) throws IOException {
        InputData inputData = objectMapper.readValue(new File(inputFilePath), InputData.class);
        System.out.println("Данные успешно прочитаны из файла: " + inputFilePath);

        User user = inputData.getUser();
        Order order = inputData.getOrder();


        boolean isUserValid = isUserValid(user.getId());
        int totalSum = calculateTotalSum(order.getItems());
        boolean discountApplied = false;

        if (isUserValid) {
            System.out.println("Пользователь с ID " + user.getId() + " является доверенным.");

            if (totalSum > 10000) {
                System.out.println("Сумма заказа (" + totalSum + ") превышает 10000. Применяется скидка.");
                totalSum = applyDiscount(totalSum, 5);
                discountApplied = true;
            } else {
                System.out.println("Сумма заказа (" + totalSum + ") не превышает 10000. Скидка не применяется.");
            }
        } else {
            System.out.println("Пользователь с ID " + user.getId() + " не найден в списке доверенных.");
        }

        String status;
        String message;

        if (isUserValid) {
            status = "success";
            message = "Заказ для пользователя " + user.getName() + " успешно обработан.";
        } else {
            status = "error";
            message = "Пользователь с ID " + user.getId() + " не авторизован для совершения покупки.";
            totalSum = 0;
        }

        OutputData outputData = new OutputData(
                status,
                message,
                totalSum,
                discountApplied
        );

        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(outputFilePath), outputData);
        System.out.println("Результат успешно записан в файл: " + outputFilePath);
    }

    private int calculateTotalSum(List<Item> items) {
        return items.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private boolean isUserValid(int userId) {
        return TRUSTED_USER_IDS.contains(userId);
    }

    private int applyDiscount(int sum, int discountPercent) {
        double discountValue = sum * (discountPercent / 100.0);
        return (int) Math.round(sum - discountValue);
    }
}
