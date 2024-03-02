package com.roy.drisk.connector.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class ArgumentPreparedStatementSetter implements PreparedStatementSetter {
    private final Object[] args;

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    public void setValues(PreparedStatement ps) throws SQLException {
        if (this.args != null) {
            for (int i = 0; i < this.args.length; i++) {
                doSetValue(ps, i + 1, this.args[i]);
            }
        }
    }

    protected void doSetValue(PreparedStatement ps, int position, Object value) throws SQLException {
        ps.setObject(position, value);
    }
}
