package ru.otus.numbers.client;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.numbers.grpc.NumbersProto.*;
import ru.otus.numbers.grpc.NumbersServiceGrpc;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class NumbersClient {

    private static final Logger log = LoggerFactory.getLogger(NumbersClient.class);

    @GrpcClient("numberServer")
    private NumbersServiceGrpc.NumbersServiceStub asyncStub;

    public void run(int startValue, int endValue) throws InterruptedException {
        log.info("numbers Client is starting...");

        AtomicInteger currentValue = new AtomicInteger(startValue);
        AtomicBoolean newValueReceived = new AtomicBoolean(false);

        ClientStreamObserver streamObserver = new ClientStreamObserver(currentValue, newValueReceived);

        SequenceRequest request = SequenceRequest.newBuilder()
                .setFirstValue(0)
                .setLastValue(endValue)
                .build();

        asyncStub.getSequence(request, streamObserver);

        for (int i = 0; i <= 50; i++) {
            Thread.sleep(1000);

            int base = currentValue.get();

            if (newValueReceived.compareAndSet(true, false)) {
                int increment = streamObserver.getLatestServerValue() + 1;
                currentValue.set(base + increment);
            } else {
                currentValue.incrementAndGet();
            }

            log.info("currentValue:{}", currentValue.get());
        }

        Thread.sleep(2000);
    }

    public static class ClientStreamObserver implements StreamObserver<SequenceResponse> {

        private final AtomicInteger currentValueRef;
        private final AtomicBoolean flagRef;

        private volatile int latestServerValue = -1;

        public ClientStreamObserver(AtomicInteger currentValueRef, AtomicBoolean flagRef) {
            this.currentValueRef = currentValueRef;
            this.flagRef = flagRef;
        }

        public int getLatestServerValue() {
            return latestServerValue;
        }

        @Override
        public void onNext(SequenceResponse response) {
            this.latestServerValue = response.getValue();
            this.flagRef.set(true);

            System.out.println("[" + java.time.LocalTime.now() + "] [grpc-default-executor] INFO r.o.n.c.ClientStreamObserver - new value:" + latestServerValue);
        }

        @Override
        public void onError(Throwable t) {
            System.err.println("Stream error: " + t.getMessage());
        }

        @Override
        public void onCompleted() {
            System.out.println("[" + java.time.LocalTime.now() + "] [grpc-default-executor] INFO r.o.n.c.ClientStreamObserver - request completed");
        }
    }
}