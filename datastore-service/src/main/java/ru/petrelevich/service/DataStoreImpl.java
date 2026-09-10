package ru.petrelevich.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.petrelevich.domain.Message;
import ru.petrelevich.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class DataStoreImpl implements DataStore {

    private final MessageRepository repository;

    @Override
    public Mono<Long> saveMessage(String roomId, Message message) {
        // Преобразуем доменный объект в сущность для сохранения
        MessageEntity entity = new MessageEntity(null, roomId, message.messageStr());
        return repository.save(entity)
                .map(MessageEntity::getId); // Возвращаем ID сохраненного сообщения
    }

    @Override
    public Flux<Message> loadAllMessages() {
        // Загрузка всего подряд без фильтрации
        return repository.findAll()
                .sort((a, b) -> a.getId().compareTo(b.getId())) // Сортируем по ID
                .map(e -> new Message(e.getMsgText()));
    }

    @Override
    public Flux<Message> loadMessages(String roomId) {
        return repository.findByRoomId(roomId)
                .sort((a, b) -> a.getId().compareTo(b.getId()))
                .map(e -> new Message(e.getMsgText()));
    }
}