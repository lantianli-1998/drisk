package com.roy.drisk.connector.hbase;

import com.roy.drisk.connector.config.ConnectorProperties;
import com.roy.drisk.connector.service.ClosedStatusAware;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author roy
 * @date 2021/10/27
 * @desc HBase连接器，用于创建及管理HBase客户端
 */
public class HBaseConnector implements AutoCloseable, ClosedStatusAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseConnector.class);
    private Properties properties = new Properties();
    private Configuration configuration;
    private HBaseConnection connection;

    public HBaseConnector(Properties properties) {
        this.properties = ConnectorProperties.filterProperties(properties, HBaseConstants.PROP_KEY);
        LOGGER.info("Create HBaseConnector with {}", this.properties);
        initConnection();
    }

    private void initConnection() {
        this.configuration = initConfiguration();
        try {
            this.connection = new HBaseConnection(ConnectionFactory.createConnection(this.configuration));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Configuration initConfiguration() {
        Configuration conf = HBaseConfiguration.create();
        conf.set(HConstants.ZOOKEEPER_QUORUM,
                properties.getProperty(HBaseConstants.ZOOKEEPER_QUORUM));
        conf.set(HConstants.ZOOKEEPER_CLIENT_PORT,
                properties.getProperty(HBaseConstants.ZOOKEEPER_CLIENT_PORT));
        conf.set(HConstants.HBASE_CLIENT_SCANNER_TIMEOUT_PERIOD,
                properties.getProperty(HBaseConstants.HBASE_CLIENT_SCANNER_TIMEOUT_PERIOD));
        conf.set("hbase.hconnection.threads.max",
                properties.getProperty(HBaseConstants.HBASE_CONNECTION_THREADS_MAX));
        conf.set("hbase.hconnection.threads.core",
                properties.getProperty(HBaseConstants.HBASE_CONNECTION_THREADS_CORE));
        conf.set(HConstants.HBASE_RPC_TIMEOUT_KEY,
                properties.getProperty(HBaseConstants.HBASE_RPC_TIMEOUT_KEY));
        return conf;
    }

    public HBaseConnection getConnection() {
        return connection;
    }

    public boolean isClosed() {
        return connection.isClosed();
    }

    @Override
    public void close() {
        try {
            LOGGER.info("HBaseConnector is closing...");
            connection.close();
            LOGGER.info("HBaseConnector closed.");
        } catch (IOException e) {
            LOGGER.warn("HBaseConnectorException", e);
        }
    }
}
