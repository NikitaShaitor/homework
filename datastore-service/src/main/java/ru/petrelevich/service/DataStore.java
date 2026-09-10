package ru.petrelevich.service;

import reactor.core.publisher.Flux;
import ru.petrelevich.domain.Message;

public interface DataStore {
    Mono<Long> saveMessage(String roomId, Message message);

    Flux<Message> loadAllMessages();

    Flux<Message> loadMessages(String roomId);
}