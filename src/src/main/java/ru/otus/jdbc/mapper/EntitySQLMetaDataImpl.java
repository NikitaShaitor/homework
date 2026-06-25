package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClassMetaData;
    private final String tableName;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        this.tableName = entityClassMetaData.getName().toLowerCase();
    }

    private String getColumnName(Field field) {
        return field.getName();
    }

    @Override
    public String getSelectAllSql() {
        String columns = entityClassMetaData.getAllFields().stream()
                .map(this::getColumnName)
                .collect(Collectors.joining(", "));
        return "SELECT " + columns + " FROM " + tableName + ";";
    }

    @Override
    public String getSelectByIdSql() {
        Field idField = entityClassMetaData.getIdField();
        return "SELECT " + getColumnName(idField) + " FROM " + tableName + " WHERE " + getColumnName(idField) + " = ?;";
    }

    @Override
    public String getInsertSql() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        Field idField = entityClassMetaData.getIdField();

        String columns = fieldsWithoutId.stream()
                .map(this::getColumnName)
                .collect(Collectors.joining(", "));
        String placeholders = fieldsWithoutId.stream()
                .map(f -> "?")
                .collect(Collectors.joining(", "));

        return "INSERT INTO " + tableName + " (" + columns + ", " + getColumnName(idField) + ") VALUES (" + placeholders + ", ?);";
    }

    @Override
    public String getUpdateSql() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        Field idField = entityClassMetaData.getIdField();

        String setClause = fieldsWithoutId.stream()
                .map(f -> getColumnName(f) + " = ?")
                .collect(Collectors.joining(", "));

        return "UPDATE " + tableName + " SET " + setClause + " WHERE " + getColumnName(idField) + " = ?;";
    }
}