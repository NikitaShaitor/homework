package ru.otus;

public class Main {
    static void main() {
        testStringPool();

    }

    static void testStringPool() {
        var s2 = "java" + 5;
        for (int i = 1; i < 7; i++) {
            var s = "java" + i;
            System.out.println(" == " + (s == s2) + "   equals: " + s.equals(s2));
        }
    }
}
