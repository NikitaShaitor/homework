package ru.otus.numbers.server;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.otus.numbers.grpc.NumbersProto.*;
import ru.otus.numbers.grpc.NumbersServiceGrpc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@GrpcService
@RequiredArgsConstructor
public class NumbersServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {

    @Override
    public void getSequence(SequenceRequest request, StreamObserver<SequenceResponse> responseObserver) {
        int firstValue = request.getFirstValue();
        int lastValue = request.getLastValue();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            if (firstValue > lastValue) {
                scheduler.shutdown();
                responseObserver.onCompleted();
                return;
            }

            SequenceResponse response = SequenceResponse.newBuilder()
                    .setValue(firstValue)
                    .build();

            responseObserver.onNext(response);
            firstValue++;
        }, 0, 2, TimeUnit.SECONDS);

        responseObserver.setOnCancelHandler(scheduler::shutdownNow);
    }
}