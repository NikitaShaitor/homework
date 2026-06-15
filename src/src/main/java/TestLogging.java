public class TestLogging implements TestLoggingInterface {

    @Override
    @Log
    public void calculation(int param1) {
        System.out.println("Реальный расчет с одним параметром: " + param1);
    }

    @Override
    @Log
    public void calculation(int param1, int param2) {
        System.out.println("Реальный расчет с двумя параметрами: " + param1 + ", " + param2);
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        System.out.println("Реальный расчет с тремя параметрами: " + param1 + ", " + param2 + ", " + param3);
    }
}
