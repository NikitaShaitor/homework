package ru.otus.references;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * VM options: -Xmx256m -Xms256m -Xlog:gc=debug
 */
@SuppressWarnings({"java:S125", "java:S1144", "java:S1215", "java:S1854", "java:S1481"})
public class ReferenceDemo {
    private static final Logger logger = LoggerFactory.getLogger(ReferenceDemo.class);

    public static void main(String[] args) throws InterruptedException {
        strong();
    }

    private static void strong() {
        var size = 1010;
        List<BigObject> references = new ArrayList<>(size);

        for (var idx = 0; idx < size; idx++) {
            references.add(new BigObject());
            logger.info("idx:{}", idx);
        }
        logger.info("Size: {}", references.size());
    }

    private static void weak() {
        var size = 1010;
        List<WeakReference<BigObject>> references = new ArrayList<>(size);

        for (var idx = 0; idx < size; idx++) {
            references.add(new WeakReference<>(new BigObject()));
        }

        var sum = 0;
        for (var idx = 0; idx < size; idx++) {
            if (references.get(idx).get() != null) {
                sum++;
            }
        }
        logger.info("Weak references: {}", sum);
    }

    private static void soft() {
        var size = 1010;
        List<SoftReference<BigObject>> references = new ArrayList<>(size);

        for (var idx = 0; idx < size; idx++) {
            references.add(new SoftReference<>(new BigObject()));
        }

        var sum = 0;
        for (var idx = 0; idx < size; idx++) {
            if (references.get(idx).get() != null) {
                sum++;
            }
        }

        logger.info("Soft references: {}", sum);
    }

    private static void softWithReferenceQueue() {
        var size = 1010;

        ReferenceQueue<BigObject> refQueue = new ReferenceQueue<>();

        List<SoftReference<BigObject>> references = new ArrayList<>(size);

        for (var idx = 0; idx < size; idx++) {
            references.add(new SoftReference<>(new BigObject(), refQueue));
        }

        var sum = 0;
        for (var idx = 0; idx < size; idx++) {
            if (references.get(idx).get() != null) {
                sum++;
            }
        }

        logger.info("Soft references: {}", sum);
        while (true) {
            var r = refQueue.poll();
            if (r == null) {
                break;
            }
            references.remove(r);
        }
        logger.info("references size: {}", references.size());
    }

    private static void finalizeDemo() throws InterruptedException {
        var immortal = new Immortal(false);
        logger.info("immortal_1:{}", immortal);

        immortal = null;
        System.gc();
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));

        logger.info("immortal_2:{}", immortal);

        var immortalReal = new Immortal(true);
        logger.info("immortal next:{}", immortalReal);

        immortalReal = null;
        System.gc();
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));

        immortalReal = revivalBackup;
        logger.info("immortal next_2:{}", immortalReal);
    }

    private static void phantom() throws InterruptedException {
        var a = new BigObject();
        logger.info("a: {}", a);

        ReferenceQueue<BigObject> refQueue = new ReferenceQueue<>();

        PhantomReference<BigObject> phantomA = new PhantomReference<>(a, refQueue);
        logger.info("Ref in pool before GC: {}", refQueue.poll());

        logger.info("phantomA.get: {}", phantomA.get()); // всегда null

        a = null;

        System.gc();
        Thread.sleep(100);
        Reference<? extends BigObject> aa = refQueue.poll();
        logger.info("Ref in pool after GC: {}", aa);

        BigObject resObject = aa.get();
        logger.info("resObject: {}", resObject);
    }

    private static void phantomDemo() throws InterruptedException {
        var a = new BigObject();
        ReferenceQueue<BigObject> refQueue = new ReferenceQueue<>();
        var phantomReference = new PhantomReference<>(a, refQueue);

        new Thread(() -> {
            try {
                logger.info("Waiting for object cleaning...");
                Reference<? extends BigObject> removed = refQueue.remove();
                logger.info("Object cleaned:{}", removed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        })
                .start();
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        logger.info("cleaning...");
        a = null;
        System.gc();
        Thread.sleep(100);
        logger.info("done");
    }

    static class BigObject {
        final byte[] array = new byte[1024 * 1024];
    }

    private static Immortal revivalBackup;

    static class Immortal {
        private final boolean doRevival;

        public Immortal(boolean doRevival) {
            this.doRevival = doRevival;
        }

        @Override
        @SuppressWarnings({"deprecation", "removal", "java:S1113", "java:S2696"})
        protected void finalize() {
            logger.info("finalize it");
            if (doRevival) {
                revivalBackup = this;
            }
        }
    }
}
