package ru.otus;

import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Dispenser dispenser = new DispenserImpl();
        Atm atm = new Atm(dispenser);

        atm.deposit(1000, 5);
        atm.deposit(5000, 2);

        System.out.println("Баланс: " + atm.getBalance());

        try {
            Map<Integer, Integer> withdrawn = atm.withdraw(12000);
            System.out.println("Выдано:");
            withdrawn.forEach((nominal, count) -> System.out.println(nominal + " x " + count));

            System.out.println("Баланс после выдачи: " + atm.getBalance());

            atm.withdraw(2500);

        } catch (AtmException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
