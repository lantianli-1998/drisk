package com.roy.drisk.connector.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 数据库操作工具类
 */
public class DatabaseUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);
    private DatabaseConnector connector;
    private String dsName;

    public DatabaseUtil(DatabaseConnector connector) {
        this(connector, DatabaseConstants.DEFAULT_DS_NAME);
    }

    public DatabaseUtil(DatabaseConnector connector, String dsName) {
        this.connector = connector;
        this.dsName = dsName;
    }

    private static String getSql(Object sqlProvider) {
        if (sqlProvider instanceof SqlProvider) {
            return ((SqlProvider) sqlProvider).getSql();
        } else {
            return null;
        }
    }

    public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws SQLException {
        String sql = getSql(psc);
        LOGGER.debug("Executing PreparedStatement [{}]", sql);
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = connector.getConnection(dsName);
            stmt = psc.createPreparedStatement(con);
            return action.doInPreparedStatement(stmt);
        } catch (SQLException ex) {
            if (con != null && !con.getAutoCommit()) {
                con.rollback();
            }
            JdbcUtil.closeStatement(stmt);
            stmt = null;
            JdbcUtil.closeConnection(con);
            con = null;
            throw new SQLException("PreparedStatementCallbackException " + sql, ex);
        } finally {
            if (con != null && !con.getAutoCommit()) {
                con.commit();
            }
            JdbcUtil.closeStatement(stmt);
            JdbcUtil.closeConnection(con);
        }
    }

    public <T> T execute(String sql, PreparedStatementCallback<T> action) throws SQLException {
        return execute(new SimplePreparedStatementCreator(sql), action);
    }

    public <T> T query(PreparedStatementCreator psc, final PreparedStatementSetter pss,
                       final ResultSetExtractor<T> rse) throws SQLException {
        return execute(psc, ps -> {
            ResultSet rs = null;
            try {
                if (pss != null) {
                    pss.setValues(ps);
                }
                rs = ps.executeQuery();
                return rse.extractData(rs);
            } finally {
                JdbcUtil.closeResultSet(rs);
            }
        });
    }

    public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) throws SQLException {
        return query(psc, null, rse);
    }

    public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws SQLException {
        return query(new SimplePreparedStatementCreator(sql), pss, rse);
    }

    public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) throws SQLException {
        return query(sql, new ArgumentPreparedStatementSetter(args), rse);
    }

    public void query(PreparedStatementCreator psc, RowCallbackHandler rch) throws SQLException {
        query(psc, new RowCallbackHandlerResultSetExtractor(rch));
    }

    public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws SQLException {
        query(sql, pss, new RowCallbackHandlerResultSetExtractor(rch));
    }

    public void query(String sql, RowCallbackHandler rch, Object... args) throws SQLException {
        query(sql, new ArgumentPreparedStatementSetter(args), rch);
    }

    public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) throws SQLException {
        return query(psc, new RowMapperResultSetExtractor<>(rowMapper));
    }

    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws SQLException {
        return query(sql, pss, new RowMapperResultSetExtractor<>(rowMapper));
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws SQLException {
        return query(sql, new RowMapperResultSetExtractor<>(rowMapper), args);
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws SQLException {
        List<T> results = query(sql, new RowMapperResultSetExtractor<>(rowMapper, 1), args);
        return JdbcUtil.requiredSingleResult(results);
    }

    public Map<String, Object> queryForMap(String sql, Object... args) throws SQLException {
        return queryForObject(sql, new ColumnMapRowMapper(), args);
    }

    public List<Map<String, Object>> queryForList(String sql, Object... args) throws SQLException {
        return query(sql, new ColumnMapRowMapper(), args);
    }

    protected int update(final PreparedStatementCreator psc, final PreparedStatementSetter pss)
            throws SQLException {
        String sql = getSql(psc);
        LOGGER.debug("Executing PreparedStatement update:[{}]", sql);
        return execute(psc, ps -> {
            if (pss != null) {
                pss.setValues(ps);
            }
            int rows = ps.executeUpdate();
            LOGGER.debug("Executing PreparedStatement update:[{}], affected:[]", sql, rows);
            return rows;
        });
    }

    public int update(PreparedStatementCreator psc) throws SQLException {
        return update(psc, null);
    }

    public int update(String sql, PreparedStatementSetter pss) throws SQLException {
        return update(new SimplePreparedStatementCreator(sql), pss);
    }

    public int update(String sql, Object... args) throws SQLException {
        return update(sql, new ArgumentPreparedStatementSetter(args));
    }
}
