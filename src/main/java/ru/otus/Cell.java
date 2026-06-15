package ru.otus;

public class Cell {
    private final Banknote banknote;

    public Cell(int nominal) {
        this.banknote = new Banknote(nominal, 0);
    }

    public void deposit(int count) {
        banknote.add(count);
    }

    public boolean withdraw(int count) {
        return banknote.withdraw(count);
    }

    public int getNominal() {
        return banknote.getNominal();
    }

    public int getAvailableCount() {
        return banknote.getCount();
    }

    public int getTotalAmount() {
        return banknote.getTotalAmount();
    }
}