import java.util.List;

@FunctionalInterface
public interface SensorDataBufferedWriter {
    void write(List<SensorData> data);
}