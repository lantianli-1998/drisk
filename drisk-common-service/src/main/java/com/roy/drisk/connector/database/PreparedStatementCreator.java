package com.roy.drisk.connector.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public interface PreparedStatementCreator {
    PreparedStatement createPreparedStatement(Connection con) throws SQLException;
}
