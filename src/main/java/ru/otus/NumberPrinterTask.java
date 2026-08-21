package ru.otus;

public class NumberPrinterTask implements Runnable {
    private final int workerId;
    private final SharedState state;

    public NumberPrinterTask(int workerId, SharedState state) {
        this.workerId = workerId;
        this.state = state;
    }

    @Override
    public void run() {
        try {
            printSequence(1, 10);
            printSequence(9, 1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void printSequence(int start, int end) throws InterruptedException {
        int step = start <= end ? 1 : -1;

        for (int i = start; ; i += step) {
            if (workerId == 1) {
                state.awaitFirstTurn();
            } else {
                state.awaitSecondTurn();
            }

            System.out.println("Поток " + workerId + ": " + i);

            state.switchTurn();

            if (i == end) break;
        }
    }
}
