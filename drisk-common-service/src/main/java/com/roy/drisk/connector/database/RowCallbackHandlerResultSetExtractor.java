package com.roy.drisk.connector.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class RowCallbackHandlerResultSetExtractor implements ResultSetExtractor<Object> {
    private final RowCallbackHandler rch;

    public RowCallbackHandlerResultSetExtractor(RowCallbackHandler rch) {
        this.rch = rch;
    }

    @Override
    public Object extractData(ResultSet rs) throws SQLException {
        while (rs.next()) {
            this.rch.processRow(rs);
        }
        return null;
    }
}
