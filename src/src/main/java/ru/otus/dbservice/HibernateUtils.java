package ru.otus.dbservice;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtils {
    private static EntityManagerFactory entityManagerFactory;

    public static synchronized EntityManagerFactory buildEntityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
        }
        return entityManagerFactory;
    }
}