package ru.otus;

public class Banknote {
    private final int nominal;
    private int count;

    public Banknote(int nominal, int count) {
        this.nominal = nominal;
        this.count = count;
    }

    public int getNominal() {
        return nominal;
    }

    public int getCount() {
        return count;
    }

    public void add(int amount) {
        this.count += amount;
    }

    public boolean withdraw(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return true;
        }
        return false;
    }

    public int getTotalAmount() {
        return nominal * count;
    }
}
