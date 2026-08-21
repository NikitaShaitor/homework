import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class SensorDataProcessorBufferedTest {

    @Test
    void shouldFlushWhenBufferIsFull() {
        List<List<SensorData>> writtenBatches = new ArrayList<>();

        SensorDataBufferedWriter writer = batch -> writtenBatches.add(batch);
        SensorDataProcessorBuffered processor = new SensorDataProcessorBuffered(3, writer);

        processor.process(new SensorData(3, 30));
        processor.process(new SensorData(1, 10));
        processor.process(new SensorData(2, 20));

        assertThat(writtenBatches).hasSize(1);
        assertThat(writtenBatches.get(0)).extracting(SensorData::getValue)
                .containsExactly(10.0, 20.0, 30.0);
    }

    @Test
    void shouldNotFlushBeforeBufferIsFull() {
        List<List<SensorData>> writtenBatches = new ArrayList<>();
        SensorDataBufferedWriter writer = batch -> writtenBatches.add(batch);
        SensorDataProcessorBuffered processor = new SensorDataProcessorBuffered(3, writer);

        processor.process(new SensorData(1, 10));
        processor.process(new SensorData(2, 20));

        assertThat(writtenBatches).isEmpty();
        assertThat(processor.size()).isEqualTo(2);
    }

    @Test
    void manualFlushShouldWritePartialBuffer() {
        List<List<SensorData>> writtenBatches = new ArrayList<>();
        SensorDataBufferedWriter writer = batch -> writtenBatches.add(batch);
        SensorDataProcessorBuffered processor = new SensorDataProcessorBuffered(5, writer);

        processor.process(new SensorData(1, 10));
        processor.process(new SensorData(2, 20));

        processor.flush();

        assertThat(writtenBatches).hasSize(1);
        assertThat(writtenBatches.get(0)).extracting(SensorData::getValue)
                .containsExactly(10.0, 20.0);
        assertThat(processor.size()).isZero();
    }

    @Test
    void multipleFillsAndFlushes() {
        List<List<SensorData>> writtenBatches = new ArrayList<>();
        SensorDataBufferedWriter writer = batch -> writtenBatches.add(batch);
        SensorDataProcessorBuffered processor = new SensorDataProcessorBuffered(2, writer);

        processor.process(new SensorData(2, 20));
        processor.process(new SensorData(1, 10)); // Flush #1
        processor.process(new SensorData(4, 40));
        processor.process(new SensorData(3, 30)); // Flush #2
        processor.flush();

        assertThat(writtenBatches).hasSize(2);
        assertThat(writtenBatches.get(0)).extracting(SensorData::getValue).containsExactly(10.0, 20.0);
        assertThat(writtenBatches.get(1)).extracting(SensorData::getValue).containsExactly(30.0, 40.0);
    }

    @Test
    void outOfOrderInputShouldBeSortedInBatch() {
        List<List<SensorData>> writtenBatches = new ArrayList<>();
        SensorDataBufferedWriter writer = batch -> writtenBatches.add(batch);
        SensorDataProcessorBuffered processor = new SensorDataProcessorBuffered(3, writer);

        processor.process(new SensorData(3, 30));
        processor.process(new SensorData(2, 20));
        processor.process(new SensorData(1, 10));

        assertThat(writtenBatches).hasSize(1);
        assertThat(writtenBatches.get(0)).extracting(SensorData::getTimestamp)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    void repeatedFlushOnEmptyBufferDoesNothing() {
        List<List<SensorData>> writtenBatches = new ArrayList<>();
        SensorDataBufferedWriter writer = batch -> writtenBatches.add(batch);
        SensorDataProcessorBuffered processor = new SensorDataProcessorBuffered(2, writer);

        processor.flush();
        processor.flush();

        assertThat(writtenBatches).isEmpty();
    }

    @Test
    void constructorShouldRejectInvalidArguments() {
        assertThatThrownBy(() -> new SensorDataProcessorBuffered(1, null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new SensorDataProcessorBuffered(0, batch -> {}))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
