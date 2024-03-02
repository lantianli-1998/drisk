package com.roy.drisk.connector.database;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import java.io.Closeable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author roy
 * @date 2021/10/27
 * @desc MyBatis的SqlSession静态代理
 */
public class DatabaseSession implements Closeable {
    private SqlSession session;

    public DatabaseSession(SqlSession session) {
        this.session = session;
    }

    public <T> T selectOne(String statement) {
        return session.selectOne(statement);
    }

    public int delete(String statement, Object parameter) {
        return session.delete(statement, parameter);
    }

    public void rollback() {
        session.rollback();
    }

    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return session.selectMap(statement, mapKey);
    }

    public int insert(String statement) {
        return session.insert(statement);
    }

    public <T> Cursor<T> selectCursor(String statement) {
        return session.selectCursor(statement);
    }

    public void select(String statement, ResultHandler handler) {
        session.select(statement, handler);
    }

    public <T> T selectOne(String statement, Object parameter) {
        return session.selectOne(statement, parameter);
    }

    public void clearCache() {
        session.clearCache();
    }

    public void rollback(boolean force) {
        session.rollback(force);
    }

    public void select(String statement, Object parameter, ResultHandler handler) {
        session.select(statement, parameter, handler);
    }

    public int insert(String statement, Object parameter) {
        return session.insert(statement, parameter);
    }

    public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
        return session.selectCursor(statement, parameter, rowBounds);
    }

    public <E> List<E> selectList(String statement) {
        return session.selectList(statement);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return session.selectMap(statement, parameter, mapKey);
    }

    public <T> T getMapper(Class<T> type) {
        return session.getMapper(type);
    }

    public void commit(boolean force) {
        session.commit(force);
    }

    public <E> List<E> selectList(String statement, Object parameter) {
        return session.selectList(statement, parameter);
    }

    public void commit() {
        session.commit();
    }

    public <T> Cursor<T> selectCursor(String statement, Object parameter) {
        return session.selectCursor(statement, parameter);
    }

    public Connection getConnection() {
        return session.getConnection();
    }

    @Override
    public void close() {
        session.close();
    }

    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        session.select(statement, parameter, rowBounds, handler);
    }

    public int update(String statement) {
        return session.update(statement);
    }

    public int delete(String statement) {
        return session.delete(statement);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return session.selectMap(statement, parameter, mapKey, rowBounds);
    }

    public int update(String statement, Object parameter) {
        return session.update(statement, parameter);
    }

    public List<BatchResult> flushStatements() {
        return session.flushStatements();
    }

    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return session.selectList(statement, parameter, rowBounds);
    }
}
