package ru.otus;

import java.util.*;

public class Atm {
    private final Dispenser dispenser;

    public Atm(Dispenser dispenser) {
        this.dispenser = dispenser;
    }

    public void deposit(int nominal, int count) throws AtmException {
        validateDepositParams(nominal, count);

        Cell cell = dispenser.getCell(nominal)
                .orElseGet(() -> {
                    Cell newCell = new Cell(nominal);
                    dispenser.addCell(newCell);
                    return newCell;
                });
        cell.deposit(count);
    }

    public Map<Integer, Integer> withdraw(int amount) throws AtmException {
        validateWithdrawalAmount(amount);

        if (amount > dispenser.getTotalAmount()) {
            throw new AtmException("В банкомате недостаточно средств.");
        }

        var withdrawalPlan = calculateWithdrawalPlan(amount);

        if (!withdrawalPlan.isSuccess()) {
            throw new AtmException(withdrawalPlan.getErrorMessage());
        }

        executeWithdrawalPlan(withdrawalPlan.getPlan());

        return withdrawalPlan.getPlan();
    }

    public int getBalance() {
        return dispenser.getTotalAmount();
    }


    private WithdrawalResult calculateWithdrawalPlan(int remainingAmount) {
        Map<Integer, Integer> plan = new LinkedHashMap<>();
        int amountLeft = remainingAmount;

        List<Cell> sortedCells = new ArrayList<>(dispenser.getCells());
        sortedCells.sort(Comparator.comparingInt(Cell::getNominal).reversed());

        for (Cell cell : sortedCells) {
            int nominal = cell.getNominal();
            int availableCount = cell.getAvailableCount();

            if (availableCount == 0 || nominal > amountLeft) {
                continue;
            }

            int neededBills = amountLeft / nominal;
            int billsToWithdraw = Math.min(neededBills, availableCount);

            if (billsToWithdraw > 0) {
                plan.put(nominal, billsToWithdraw);
                amountLeft -= billsToWithdraw * nominal;
            }

            if (amountLeft == 0) {
                break;
            }
        }

        return amountLeft == 0 ?
                new WithdrawalResult(plan) :
                new WithdrawalResult("Невозможно выдать сумму " + remainingAmount + ". Не хватает купюр нужного номинала.");
    }

    private void executeWithdrawalPlan(Map<Integer, Integer> plan) {
        for (var entry : plan.entrySet()) {
            int nominal = entry.getKey();
            int count = entry.getValue();

            dispenser.getCell(nominal)
                    .ifPresent(cell -> cell.withdraw(count));
        }
    }

    private void validateDepositParams(int nominal, int count) throws AtmException {
        if (nominal <= 0) {
            throw new AtmException("Номинал должен быть положительным.");
        }
        if (count <= 0) {
            throw new AtmException("Количество купюр должно быть положительным.");
        }
    }

    private void validateWithdrawalAmount(int amount) throws AtmException {
        if (amount <= 0) {
            throw new AtmException("Сумма для выдачи должна быть положительной.");
        }
    }

    private static class WithdrawalResult {
        private final boolean success;
        private final Map<Integer, Integer> plan;
        private final String errorMessage;

        WithdrawalResult(Map<Integer, Integer> plan) {
            this.success = true;
            this.plan = plan;
            this.errorMessage = null;
        }

        WithdrawalResult(String errorMessage) {
            this.success = false;
            this.plan = null;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return success;
        }

        public Map<Integer, Integer> getPlan() {
            return plan;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}