package com.roy.drisk.connector.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */

public class SimplePreparedStatementCreator implements PreparedStatementCreator, SqlProvider {
    private final String sql;

    public SimplePreparedStatementCreator(String sql) {
        this.sql = sql;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        return con.prepareStatement(this.sql);
    }

    @Override
    public String getSql() {
        return this.sql;
    }
}
