package com.roy.drisk.connector.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Collection;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc JDBC工具类
 */
public class JdbcUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUtil.class);

    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                LOGGER.debug("Could not close JDBC Connection", ex);
            } catch (Throwable ex) {
                LOGGER.debug("Unexpected exception on closing JDBC Connection", ex);
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                LOGGER.debug("Could not close JDBC Statement", ex);
            } catch (Throwable ex) {
                LOGGER.debug("Unexpected exception on closing JDBC Statement", ex);
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                LOGGER.debug("Could not close JDBC ResultSet", ex);
            } catch (Throwable ex) {
                LOGGER.debug("Unexpected exception on closing JDBC ResultSet", ex);
            }
        }
    }

    public static <T> T requiredSingleResult(Collection<T> results) {
        int size = (results != null ? results.size() : 0);
        if (size == 0) {
            throw new RuntimeException("ResultSet is empty.");
        }
        if (results.size() > 1) {
            throw new RuntimeException("ResultSet size too large: " + size);
        }
        return results.iterator().next();
    }

    public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
        String name = resultSetMetaData.getColumnLabel(columnIndex);
        if (name == null || name.length() < 1) {
            name = resultSetMetaData.getColumnName(columnIndex);
        }
        return name;
    }

    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }
        if (obj instanceof Blob) {
            Blob blob = (Blob) obj;
            obj = blob.getBytes(1, (int) blob.length());
        } else if (obj instanceof Clob) {
            Clob clob = (Clob) obj;
            obj = clob.getSubString(1, (int) clob.length());
        } else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
            obj = rs.getTimestamp(index);
        } else if (className != null && className.startsWith("oracle.sql.DATE")) {
            String metaDataClassName = rs.getMetaData().getColumnClassName(index);
            if ("java.sql.Timestamp".equals(metaDataClassName) || "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
                obj = rs.getTimestamp(index);
            } else {
                obj = rs.getDate(index);
            }
        } else if (obj != null && obj instanceof java.sql.Date) {
            if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
                obj = rs.getTimestamp(index);
            }
        }
        return obj;
    }
}
