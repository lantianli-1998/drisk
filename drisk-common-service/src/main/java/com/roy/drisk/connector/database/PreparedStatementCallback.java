package com.roy.drisk.connector.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public interface PreparedStatementCallback<T> {
    T doInPreparedStatement(PreparedStatement ps) throws SQLException;
}
