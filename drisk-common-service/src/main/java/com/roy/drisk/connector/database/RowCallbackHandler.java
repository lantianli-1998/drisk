package com.roy.drisk.connector.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public interface RowCallbackHandler {
    void processRow(ResultSet rs) throws SQLException;
}
