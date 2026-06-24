package ru.otus;

import java.util.HashMap;
import java.util.Map;

public class ATM {
    private Map<Integer, Integer> atmCells = new HashMap<>();

    public ATM(Map<Integer, Integer> initialState) {
        this.atmCells = new HashMap<>(initialState);
    }

    public ATMMemento saveState() {
        System.out.println("ATM: Сохранение состояния.");
        return new ATMMemento(atmCells);
    }

    public void restoreState(ATMMemento memento) {
        System.out.println("ATM: Восстановление состояния.");
        this.atmCells = memento.getState();
    }

    public void withdraw(int nominal, int count) {
        if (atmCells.containsKey(nominal)) {
            int currentCount = atmCells.get(nominal);
            if (currentCount >= count) {
                atmCells.put(nominal, currentCount - count);
                System.out.println("Выдано: " + count + " купюр по " + nominal);
            } else {
                System.out.println("Недостаточно купюр номиналом " + nominal);
            }
        } else {
            System.out.println("Номинал " + nominal + " отсутствует в банкомате.");
        }
    }

    public int getBalance() {
        return atmCells.entrySet().stream()
                .mapToInt(entry -> entry.getKey() * entry.getValue())
                .sum();
    }

    public Map<Integer, Integer> getAtmCells() {
        return atmCells;
    }
}
