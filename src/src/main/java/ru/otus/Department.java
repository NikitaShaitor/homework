package ru.otus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Department {
    private final List<ATM> atms = new ArrayList<>();
    private final Map<ATM, ATMMemento> initialStates = new HashMap<>();

    public void addATM(ATM atm) {
        atms.add(atm);
        initialStates.put(atm, atm.saveState());
    }

    public int getTotalBalance() {
        return atms.stream()
                .mapToInt(ATM::getBalance)
                .sum();
    }

    public void restoreAllATMsToInitialState() {
        for (ATM atm : atms) {
            ATMMemento initialState = initialStates.get(atm);
            if (initialState != null) {
                atm.restoreState(initialState);
            }
        }
        System.out.println("Департамент: Все банкоматы восстановлены до начального состояния.");
    }
}
