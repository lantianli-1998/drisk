package com.roy.drisk.connector.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc ResultSet处理器
 */
public interface ResultSetExtractor<T> {
    T extractData(ResultSet rs) throws SQLException;
}
