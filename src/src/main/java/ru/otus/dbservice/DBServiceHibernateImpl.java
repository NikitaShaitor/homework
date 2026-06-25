package ru.otus.dbservice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;

import java.util.Optional;
import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService<UserDataSet> {
    private static final Logger logger = LoggerFactory.getLogger(DBServiceHibernateImpl.class);
    private final HwCache<Long, UserDataSet> cache = new MyCache<>();

    @Override
    public Optional<UserDataSet> read(long id) {
        logger.info("Attempting to read user with ID {} from cache or DB...", id);
        UserDataSet user = cache.getOrLoad(id, () -> loadFromDb(id));
        return Optional.ofNullable(user);
    }

    private UserDataSet loadFromDb(Long id) {
        logger.info("\tLoading user with ID {} from database...", id);
        return runInSession(session -> session.find(UserDataSet.class, id));
    }

    @Override
    public long save(UserDataSet dataSet) {
        Long id = runInSession(session -> {
            var tx = session.beginTransaction();
            session.save(dataSet);
            tx.commit();
            return dataSet.getId();
        });
        cache.put(id, dataSet);
        return id;
    }

    private <R> R runInSession(Function<EntityManager, R> function) {
        var emf = HibernateUtils.buildEntityManagerFactory();
        try (var em = emf.createEntityManager()) {
            return function.apply(em);
        }
    }
}