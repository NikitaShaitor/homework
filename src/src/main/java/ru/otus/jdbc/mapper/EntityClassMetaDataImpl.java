package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Default constructor not found for " + clazz.getName(), e);
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.getName().equals("id") || f.getName().equals("no"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Id field not found in class " + clazz.getName()));
    }

    @Override
    public List<Field> getAllFields() {
        return new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        Field idField = getIdField();
        return getAllFields().stream()
                .filter(field -> !field.getName().equals(idField.getName()))
                .toList();
    }
}