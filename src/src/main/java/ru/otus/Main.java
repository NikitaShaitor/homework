package ru.otus;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Map<Integer, Integer> stateATM1 = new HashMap<>();
        stateATM1.put(100, 50);
        stateATM1.put(200, 30);
        stateATM1.put(500, 20);

        Map<Integer, Integer> stateATM2 = new HashMap<>();
        stateATM2.put(50, 100);
        stateATM2.put(1000, 10);

        ATM atm1 = new ATM(stateATM1);
        ATM atm2 = new ATM(stateATM2);

        Department department = new Department();
        department.addATM(atm1);
        department.addATM(atm2);

        System.out.println("--- Начальное состояние ---");
        System.out.println("Общий баланс департамента: " + department.getTotalBalance());

        System.out.println("\n--- Операции с банкоматами ---");
        atm1.withdraw(100, 10);
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(atm2.getAtmCells().entrySet());
        atm2.withdraw(list.get(0).getKey(), 20);

        System.out.println("\n--- Состояние после операций ---");
        System.out.println("Общий баланс департамента: " + department.getTotalBalance());

        System.out.println("\n--- Восстановление ---");
        department.restoreAllATMsToInitialState();

        System.out.println("\n--- Состояние после восстановления ---");
        System.out.println("Общий баланс департамента: " + department.getTotalBalance());
    }
}
