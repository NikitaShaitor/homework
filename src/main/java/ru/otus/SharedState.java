package ru.otus;

public class SharedState {
    private boolean firstTurn = true;

    public synchronized void awaitFirstTurn() throws InterruptedException {
        while (!firstTurn) {
            this.wait();
        }
    }

    public synchronized void awaitSecondTurn() throws InterruptedException {
        while (firstTurn) {
            this.wait();
        }
    }

    public synchronized void switchTurn() {
        firstTurn = !firstTurn;
        this.notifyAll();
    }
}