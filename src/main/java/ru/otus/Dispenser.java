package ru.otus;

import java.util.Collection;
import java.util.Optional;

public interface Dispenser {
    void addCell(Cell cell);

    Optional<Cell> getCell(int nominal);

    Collection<Cell> getCells();

    int getTotalAmount();
}