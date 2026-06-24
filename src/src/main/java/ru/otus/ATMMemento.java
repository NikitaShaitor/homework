package ru.otus;

import java.util.HashMap;
import java.util.Map;

public class ATMMemento {
    private final Map<Integer, Integer> snapshot;

    public ATMMemento(Map<Integer, Integer> state) {
        this.snapshot = new HashMap<>(state);
    }

    public Map<Integer, Integer> getState() {
        return new HashMap<>(snapshot);
    }
}