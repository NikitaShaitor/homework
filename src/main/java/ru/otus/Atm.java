package ru.otus;

import java.util.*;

public class Atm {
    private final Dispenser dispenser;

    public Atm(Dispenser dispenser) {
        this.dispenser = dispenser;
    }

    public void deposit(int nominal, int count) {
        Cell cell = dispenser.getCell(nominal)
                .orElseGet(() -> {
                    Cell newCell = new Cell(nominal);
                    dispenser.addCell(newCell);
                    return newCell;
                });
        cell.deposit(count);
    }

    public Map<Integer, Integer> withdraw(int amount) throws AtmException {
        if (amount <= 0) {
            throw new AtmException("Сумма для выдачи должна быть положительной.");
        }
        if (amount > dispenser.getTotalAmount()) {
            throw new AtmException("В банкомате недостаточно средств.");
        }

        int remaining = amount;
        Map<Integer, Integer> result = new LinkedHashMap<>();

        List<Integer> sortedNominals = new ArrayList<>(dispenser.getCell(0).isPresent() ?
                Collections.emptyList() : ((DispenserImpl)dispenser).cells.keySet());

        List<Integer> nominals = new ArrayList<>(dispenser.getCell(0).isPresent() ?
                Collections.emptyList() : ((DispenserImpl)dispenser).cells.keySet());
        nominals.sort(Collections.reverseOrder());

        for (Integer nominal : nominals) {
            Optional<Cell> cellOpt = dispenser.getCell(nominal);
            if (cellOpt.isEmpty()) continue;
            Cell cell = cellOpt.get();
            if (cell.getAvailableCount() == 0) continue;

            int needed = remaining / nominal;
            int toWithdraw = Math.min(needed, cell.getAvailableCount());

            if (toWithdraw > 0 && cell.withdraw(toWithdraw)) {
                result.put(nominal, toWithdraw);
                remaining -= toWithdraw * nominal;
            }
            if (remaining == 0) break;
        }

        if (remaining > 0) {
            throw new AtmException("Невозможно выдать сумму " + amount + ". Не хватает купюр нужного номинала.");
        }
        return result;
    }

    public int getBalance() {
        return dispenser.getTotalAmount();
    }
}