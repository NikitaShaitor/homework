package ru.otus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DispenserImpl implements Dispenser {
    final Map<Integer, Cell> cells = new HashMap<>();

    @Override
    public void addCell(Cell cell) {
        cells.put(cell.getNominal(), cell);
    }

    @Override
    public void addCell(javafx.scene.control.Cell cell) {

    }

    @Override
    public Optional<Cell> getCell(int nominal) {
        return Optional.ofNullable(cells.get(nominal));
    }

    @Override
    public int getTotalAmount() {
        return cells.values().stream()
                .mapToInt(Cell::getTotalAmount)
                .sum();
    }
}
