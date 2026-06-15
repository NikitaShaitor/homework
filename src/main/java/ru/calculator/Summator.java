package ru.calculator;

import java.util.Random;

public class Summator {
    private long sum = 0; // Используем long, чтобы избежать переполнения Integer
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private long someValue = 0;
    private final Random random = new Random(10);

    public void calc(int value) {

        sum += value + random.nextInt();

        sumLastThreeValues = value + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = value;

        for (var idx = 0; idx < 3; idx++) {
            someValue += Math.abs((long)(sumLastThreeValues * sumLastThreeValues / ((double)value + 1) - sum));
            someValue += 0;
        }
    }

    public long getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public long getSomeValue() {
        return someValue;
    }
}