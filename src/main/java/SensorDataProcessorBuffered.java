import java.util.*;

public class SensorDataProcessorBuffered {

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final List<SensorData> buffer = new ArrayList<>();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be positive");
        }
        if (writer == null) {
            throw new NullPointerException("Writer cannot be null");
        }
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    public synchronized void process(SensorData data) {
        buffer.add(data);
        buffer.sort(Comparator.comparingLong(SensorData::getTimestamp));
        if (buffer.size() >= bufferSize) {
            flush();
        }
    }

    public synchronized void flush() {
        if (buffer.isEmpty()) return;
        writer.write(new ArrayList<>(buffer));
        buffer.clear();
    }

    public synchronized int size() {
        return buffer.size();
    }
}