package com.roy.drisk.connector.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc HBase的Connection代理类。由Connector统一关闭。
 */
public class HBaseConnection {
    private Connection connection;

    public HBaseConnection(Connection connection) {
        this.connection = connection;
    }

    public Configuration getConfiguration() {
        return connection.getConfiguration();
    }

    public BufferedMutator getBufferedMutator(TableName tableName) throws IOException {
        return connection.getBufferedMutator(tableName);
    }

    public BufferedMutator getBufferedMutator(BufferedMutatorParams params) throws IOException {
        return connection.getBufferedMutator(params);
    }

    public RegionLocator getRegionLocator(TableName tableName) throws IOException {
        return connection.getRegionLocator(tableName);
    }

    public boolean isAborted() {
        return connection.isAborted();
    }

    public Table getTable(TableName tableName) throws IOException {
        return connection.getTable(tableName);
    }

    public Table getTable(TableName tableName, ExecutorService pool) throws IOException {
        return connection.getTable(tableName, pool);
    }

    void close() throws IOException {
        connection.close();
    }

    public boolean isClosed() {
        return connection.isClosed();
    }

    public void abort(String why, Throwable e) {
        connection.abort(why, e);
    }

    public Admin getAdmin() throws IOException {
        return connection.getAdmin();
    }
}
