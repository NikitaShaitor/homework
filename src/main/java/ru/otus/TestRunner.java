package ru.otus;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private static final List<TestResult> testResults = new ArrayList<>();

    public static void run(Class<?> testClass) {
        try {
            Method[] methods = testClass.getDeclaredMethods();

            List<Method> beforeMethods = getAnnotatedMethods(methods, (Class<? extends Annotation>) Before.class);
            List<Method> afterMethods = getAnnotatedMethods(methods, (Class<? extends Annotation>) After.class);
            List<Method> testMethods = getAnnotatedMethods(methods, (Class<? extends Annotation>) Test.class);

            for(Method testMethod : testMethods) {
                Object instance = testClass.newInstance();

                executeMethods(instance, beforeMethods);
                executeSingleMethod(instance, testMethod);
                executeMethods(instance, afterMethods);
            }

            printStatistics();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Method> getAnnotatedMethods(Method[] methods, Class<? extends Annotation> annotationClass) {
        List<Method> result = new ArrayList<>();
        for(Method m : methods) {
            if(m.isAnnotationPresent(annotationClass)) {
                result.add(m);
            }
        }
        return result;
    }

    private static void executeMethods(Object instance, List<Method> methods) throws InvocationTargetException, IllegalAccessException {
        for(Method m : methods) {
            m.invoke(instance);
        }
    }

    private static void executeSingleMethod(Object instance, Method method) {
        try {
            method.invoke(instance);
            testResults.add(new TestResult(method.getName(), true));
        } catch(Exception e) {
            testResults.add(new TestResult(method.getName(), false));
        }
    }

    private static void printStatistics() {
        long totalTests = testResults.size();
        long passedTests = testResults.stream().filter(TestResult::isPassed).count();
        long failedTests = totalTests - passedTests;

        System.out.println("\n=== Статистика тестов ===");
        System.out.printf("Всего тестов: %d\nПрошло успешно: %d\nУпали: %d%n",
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
