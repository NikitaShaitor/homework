package ru.otus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.lang.annotation.*;

public class TestRunner {

    public static void run(Class<?> testClass) {
        try {
            Method[] methods = testClass.getDeclaredMethods();

            List<Method> beforeMethods = getAnnotatedMethods(methods, Before.class);
            List<Method> afterMethods = getAnnotatedMethods(methods, After.class);
            List<Method> testMethods = getAnnotatedMethods(methods, Test.class);

            List<TestResult> currentTestResults = new ArrayList<>();

            for (Method testMethod : testMethods) {
                Object instance = testClass.newInstance();

                executeMethods(instance, beforeMethods);
                executeSingleMethod(instance, testMethod, currentTestResults);
                executeMethods(instance, afterMethods);
            }

            printStatistics(currentTestResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Method> getAnnotatedMethods(Method[] methods, Class<? extends Annotation> annotationClass) {
        List<Method> result = new ArrayList<>();
        for (Method m : methods) {
            if (m.isAnnotationPresent(annotationClass)) {
                result.add(m);
            }
        }
        return result;
    }

    private static void executeMethods(Object instance, List<Method> methods) throws InvocationTargetException, IllegalAccessException {
        for (Method m : methods) {
            m.invoke(instance);
        }
    }

    private static void executeSingleMethod(Object instance, Method method, List<TestResult> testResults) {
        try {
            method.invoke(instance);
            testResults.add(new TestResult(method.getName(), true));
        } catch (Exception e) {
            testResults.add(new TestResult(method.getName(), false));
        }
    }

    private static void printStatistics(List<TestResult> testResults) {
        long totalTests = testResults.size();
        long passedTests = testResults.stream()
                .filter(TestResult::isPassed)
                .count();
        long failedTests = totalTests - passedTests;

        System.out.println("\n=== Статистика тестов ===");
        System.out.printf("Всего тестов: %d\nПрошло успешно: %d\nУпало: %d%n",
                totalTests, passedTests, failedTests);
    }

    private static class TestResult {
        private String testName;
        private boolean isPassed;

        public TestResult(String testName, boolean isPassed) {
            this.testName = testName;
            this.isPassed = isPassed;
        }

        public boolean isPassed() {
            return isPassed;
        }
    }
}
