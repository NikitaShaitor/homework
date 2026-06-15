package ru.otus;

import javafx.scene.control.Cell;
import java.util.Optional;

public interface Dispenser {
    void addCell(ru.otus.Cell cell);

    void addCell(Cell cell);

    Optional<ru.otus.Cell> getCell(int nominal);

    int getTotalAmount();
}