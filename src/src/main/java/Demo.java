public class Demo {
    public static void main(String[] args) {
        TestLoggingInterface realObject = new TestLogging();

        TestLoggingInterface proxyObject = ProxyFactory.createProxy(realObject);

        System.out.println("--- Вызов через прокси ---");
        proxyObject.calculation(6);

        System.out.println("\n--- Вызов напрямую (для сравнения) ---");
        realObject.calculation(6);

        System.out.println("\n--- Вызов метода с несколькими параметрами ---");
        proxyObject.calculation(10, 20, "test");
    }
}
