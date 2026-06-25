package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> cache = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = ConcurrentHashMap.newKeySet();

    @Override
    public void put(K key, V value) {
        Objects.requireNonNull(key, "Ключ не может быть null");
        if (value == null) {
            remove(key); // Если значение null, просто удаляем из кэша
            return;
        }
        var oldValue = cache.put(key, value);
        notifyListeners(key, value, "PUT");
        logger.info("MyCache:put: {} -> {}", key, value);
    }

    @Override
    public void remove(K key) {
        var removedValue = cache.remove(key);
        if (removedValue != null) {
            notifyListeners(key, removedValue, "REMOVE");
            logger.info("MyCache:remove: {}", key);
        }
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    /**
     * Метод-обертка для получения значения.
     * Если значения нет в кэше, оно загружается через supplier и кладется в кэш.
     */
    public V getOrLoad(K key, Supplier<V> loader) {
        V value = get(key);
        if (value == null) {
            value = loader.get();
            put(key, value);
        }
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        for (var listener : listeners) {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                logger.error("Error notifying listener", e);
            }
        }
    }
}