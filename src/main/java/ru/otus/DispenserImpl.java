package ru.otus;

import java.util.*;

public class DispenserImpl implements Dispenser {
    private final Map<Integer, Cell> cells = new HashMap<>();

    @Override
    public void addCell(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("Ячейка не может быть null");
        }
        cells.put(cell.getNominal(), cell);
    }

    @Override
    public Optional<Cell> getCell(int nominal) {
        return Optional.ofNullable(cells.get(nominal));
    }

    @Override
    public Collection<Cell> getCells() {
        return Collections.unmodifiableCollection(cells.values());
    }

    @Override
    public int getTotalAmount() {
        return cells.values().stream()
                .mapToInt(Cell::getTotalAmount)
                .sum();
    }
}