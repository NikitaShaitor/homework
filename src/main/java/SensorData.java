public class SensorData {
    private final long timestamp;
    private final double value;

    public SensorData(long timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public long getTimestamp() { return timestamp; }
    public double getValue() { return value; }
}