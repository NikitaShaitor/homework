package ru.otus.numbers.client;

import io.grpc.stub.StreamObserver;
import ru.otus.numbers.grpc.NumbersProto.SequenceResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
public class ClientStreamObserver implements StreamObserver<SequenceResponse> {

    private final AtomicInteger currentValue;
    private final AtomicBoolean newValueReceived;

    @Setter
    private volatile int latestServerValue = -1;

    @Override
    public void onNext(SequenceResponse response) {
        this.latestServerValue = response.getValue();
        this.newValueReceived.set(true);
        log.info("new value:{}", latestServerValue);
    }

    @Override
    public void onError(Throwable t) {
        log.error("Stream error", t);
    }

    @Override
    public void onCompleted() {
        log.info("request completed");
    }
}
