package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        HwListener<String, Integer> listener = (key, value, action) ->
                logger.info("Cache event - key:{}, value:{}, action:{} ", key, value, action);

        cache.addListener(listener);

        cache.put("A", 10);
        logger.info("get A: {}", cache.get("A"));

        cache.put("B", 20);
        logger.info("get B: {}", cache.get("B"));

        cache.remove("A");
        logger.info("get A after removal: {}", cache.get("A"));

        cache.removeListener(listener);
    }
}