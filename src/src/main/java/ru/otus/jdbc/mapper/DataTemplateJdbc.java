package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.repository.executor.DbExecutorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor,
                            EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) throws DbExecutorException {
        String sql = entitySQLMetaData.getSelectByIdSql();
        return Optional.ofNullable(dbExecutor.executeSelect(connection, sql, List.of(id), this::mapResultSetToObject));
    }

    @Override
    public List<T> findAll(Connection connection) {
        String sql = entitySQLMetaData.getSelectAllSql();
        return dbExecutor.executeSelect(connection, sql, List.of(), this::mapResultSetToList);
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            String sql = entitySQLMetaData.getInsertSql();
            List<Object> params = getParamsFromObject(object, entityClassMetaData.getAllFields());
            return dbExecutor.executeInsert(connection, sql, params);
        } catch (IllegalAccessException e) {
            throw new DbExecutorException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            String sql = entitySQLMetaData.getUpdateSql();
            List<Field> fields = entityClassMetaData.getFieldsWithoutId();
            Field idField = entityClassMetaData.getIdField();

            List<Object> params = getParamsFromObject(object, fields);
            idField.setAccessible(true);
            params.add(idField.get(object));

            dbExecutor.executeUpdate(connection, sql, params);
        } catch (IllegalAccessException e) {
            throw new DbExecutorException(e);
        }
    }

    @Override
    public void executeUpdate(Connection connection, String sql, List<Object> params) throws SQLException {
        try (var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }
            preparedStatement.executeUpdate();
        }
    }

    private T mapResultSetToObject(ResultSet rs) {
        try {
            Constructor<T> constructor = entityClassMetaData.getConstructor();
            T newInstance = constructor.newInstance();

            for (Field field : entityClassMetaData.getAllFields()) {
                field.setAccessible(true);
                Object value = rs.getObject(field.getName());
                field.set(newInstance, value);
            }
            return newInstance;
        } catch (Exception e) {
            if (e instanceof SQLException sqlException) {
                throw new DbExecutorException(sqlException);
            } else {
                throw new DbExecutorException(new Exception("Error mapping object from ResultSet", e));
            }
        }
    }

    private List<T> mapResultSetToList(ResultSet rs) {
        List<T> resultList = new ArrayList<>();
        try {
            while (rs.next()) {
                resultList.add(mapResultSetToObject(rs));
            }
            return resultList;
        } catch (SQLException e) {
            throw new DbExecutorException(e);
        }
    }

    private List<Object> getParamsFromObject(T object, List<Field> fields) throws IllegalAccessException {
        List<Object> params = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            params.add(field.get(object));
        }
        return params;
    }
}