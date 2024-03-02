package com.roy.drisk.connector.service;

import com.roy.drisk.connector.config.ConnectorConfiger;
import com.roy.drisk.connector.config.ConnectorConfigerLoader;
import com.roy.drisk.connector.database.DatabaseConnector;
import com.roy.drisk.connector.database.DatabaseSession;
import com.roy.drisk.connector.hbase.HBaseConnection;
import com.roy.drisk.connector.hbase.HBaseConnector;
import com.roy.drisk.connector.http.HttpAsyncClient;
import com.roy.drisk.connector.http.HttpConnector;
import com.roy.drisk.connector.http.HttpSyncClient;
import com.roy.drisk.connector.kafka.KafkaMessageProducer;
import com.roy.drisk.connector.kafka.KafkaProducerConnector;
import com.roy.drisk.connector.redis.BaseRedisConnector;
import com.roy.drisk.connector.redis.CloseableJedisCommands;
import com.roy.drisk.connector.redis.batch.CloseableJedisBatchCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 提供了实例化{@link SwordConnector}类
 * 及获取其所管理的connector的工厂方法。
 */
public class DriskConnectorFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriskConnectorFactory.class);
    private static volatile DriskConnector connector;
    private static final Object lock = new Object();
    private static Properties properties;

    private DriskConnectorFactory() {
    }

    static {
        loadProperties();
    }

    private static void loadProperties() {
        ConnectorConfigerLoader loader = new ConnectorConfigerLoader();
        ConnectorConfiger configer = loader.getConnectorConfiger();
        if (configer == null) {
            throw new RuntimeException("Can not initialize ConnectorConfiger.");
        }
        properties = configer.getProperties();
        LOGGER.info("Load additional properties: {}", properties);
    }

    private static DriskConnector get() {
        if (connector == null || connector.isClosed()) {
            synchronized (lock) {
                if (connector == null || connector.isClosed()) {
                    LOGGER.info("Build SwordConnector...");
                    connector = new DriskConnector(properties);
                }
            }
        }
        return connector;
    }

    /**
     * 关闭<code>SwordConnector</code>
     */
    public static void close() {
        if (connector != null && !connector.isClosed()) {
            synchronized (lock) {
                if (connector != null && !connector.isClosed()) {
                    LOGGER.info("Closing SwordConnector...");
                    connector.close();
                }
            }
        }
    }

    public static HBaseConnector getHBaseConnector() {
        return get().getHBaseConnector();
    }

    public static HBaseConnection getHBaseConnection() {
        return getHBaseConnector().getConnection();
    }

    public static HttpConnector getHttpConnector() {
        return get().getHttpConnector();
    }

    public static HttpSyncClient getHttpClient() {
        return getHttpConnector().getSyncClient();
    }

    public static HttpAsyncClient getHttpAsyncClient() {
        return getHttpConnector().getAsyncClient();
    }

    public static KafkaProducerConnector<String, String> geKafkaProducerConnector() {
        return get().getKafkaProducerConnector();
    }

    public static KafkaMessageProducer<String, String> getKafkaMessageProducer() {
        return geKafkaProducerConnector().getMessageProducer();
    }

    public static BaseRedisConnector getRedisConnector() {
        return get().getRedisConnector();
    }

    public static CloseableJedisCommands getRedisClient() {
        return getRedisConnector().getClient();
    }
    
    public static CloseableJedisBatchCommands getRedisBatch() {
        return getRedisConnector().getBatchClient();
    }

    public static DatabaseConnector getDatabaseConnector() {
        return get().getDatabaseConnector();
    }

    public static Connection getDatabaseConnection() throws SQLException {
        return getDatabaseConnector().getConnection();
    }

    public static Connection getDatabaseConnection(String dsName) throws SQLException {
        return getDatabaseConnector().getConnection(dsName);
    }

    public static DatabaseSession getDatabaseSession() throws SQLException {
        return getDatabaseConnector().getSqlSession();
    }

    public static DatabaseSession getDatabaseSession(boolean autoCommit) throws SQLException {
        return getDatabaseConnector().getSqlSession(autoCommit);
    }

    public static DatabaseSession getDatabaseSession(String dsName) throws SQLException {
        return getDatabaseConnector().getSqlSession(dsName);
    }

    public static DatabaseSession getDatabaseSession(String dsName, boolean autoCommit) throws SQLException {
        return getDatabaseConnector().getSqlSession(dsName, autoCommit);
    }
}
