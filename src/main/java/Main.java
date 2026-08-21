public class Main {
    public static void main(String[] args) {
        SensorDataBufferedWriter writer = list -> {
            System.out.println("Flush:");
            list.forEach(d ->
                    System.out.println(d.getTimestamp() + " -> " + d.getValue()));
        };

        SensorDataProcessorBuffered processor =
                new SensorDataProcessorBuffered(3, writer);

        processor.process(new SensorData(3, 30));
        processor.process(new SensorData(1, 10));
        processor.process(new SensorData(2, 20));

        processor.process(new SensorData(5, 50));
        processor.process(new SensorData(4, 40));
        processor.flush();
    }
}
