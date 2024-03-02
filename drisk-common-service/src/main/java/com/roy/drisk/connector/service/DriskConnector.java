package com.roy.drisk.connector.service;

import com.roy.drisk.connector.config.ConnectorProperties;
import com.roy.drisk.connector.database.DatabaseConnector;
import com.roy.drisk.connector.hbase.HBaseConnector;
import com.roy.drisk.connector.http.HttpConnector;
import com.roy.drisk.connector.http.PoolingHttpConnector;
import com.roy.drisk.connector.kafka.KafkaProducerConnector;
import com.roy.drisk.connector.redis.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class DriskConnector implements AutoCloseable, ClosedStatusAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriskConnector.class);
    private Properties properties = new Properties();
    private volatile HBaseConnector hBaseConnector;
    private final Object hBaseLock = new Object();
    private volatile HttpConnector httpConnector;
    private final Object httpLock = new Object();
    private volatile KafkaProducerConnector<String, String> kafkaProducerConnector;
    private final Object kafkaProducerLock = new Object();
    private volatile BaseRedisConnector redisConnector;
    private final Object redisLock = new Object();
    private volatile DatabaseConnector databaseConnector;
    private final Object databaseLock = new Object();
    private AtomicBoolean closed = new AtomicBoolean(false);

    public DriskConnector(Properties properties) {
        this.properties = ConnectorProperties.newProperties(properties);
        LOGGER.info("Create SwordConnector with {}", this.properties);
    }

    private static boolean isNotValid(ClosedStatusAware connector) {
        return connector == null || connector.isClosed();
    }

    /**
     * 取HBase连接
     *
     * @return HBaseConnector
     */
    public HBaseConnector getHBaseConnector() {
        if (isNotValid(hBaseConnector)) {
            synchronized (hBaseLock) {
                if (isNotValid(hBaseConnector)) {
                    hBaseConnector = new HBaseConnector(properties);
                }
            }
        }
        return hBaseConnector;
    }

    /**
     * 取Http连接
     *
     * @return HttpConnector
     */
    public HttpConnector getHttpConnector() {
        if (isNotValid(httpConnector)) {
            synchronized (httpLock) {
                if (isNotValid(httpConnector)) {
                    httpConnector = new PoolingHttpConnector(properties);
                }
            }
        }
        return httpConnector;
    }

    /**
     * 取KafkaProducerConnector连接，目前key和value只支持Integer和String
     *
     * @return KafkaProducerConnector
     */
    public KafkaProducerConnector<String, String> getKafkaProducerConnector() {
        if (isNotValid(kafkaProducerConnector)) {
            synchronized (kafkaProducerLock) {
                if (isNotValid(kafkaProducerConnector)) {
                    kafkaProducerConnector = new KafkaProducerConnector<>(properties);
                }
            }
        }
        return kafkaProducerConnector;
    }

    private RedisMode getRedisMode() {
        String mode = properties.getProperty(RedisConstants.MODE);
        return RedisMode.valueOf(mode.toUpperCase());
    }

    /**
     * 取Redis连接
     *
     * @return BaseRedisConnector
     */
    public BaseRedisConnector getRedisConnector() {
        if (isNotValid(redisConnector)) {
            synchronized (redisLock) {
                if (isNotValid(redisConnector)) {
                    if (getRedisMode() == RedisMode.CLUSTER) {
                        redisConnector = new RedisClusterConnector(properties);
                    } else {
                        redisConnector = new RedisStandaloneConnector(properties);
                    }
                }
            }
        }
        return redisConnector;
    }

    /**
     * 取Database连接
     *
     * @return DatabaseConnector
     */
    public DatabaseConnector getDatabaseConnector() {
        if (isNotValid(databaseConnector)) {
            synchronized (databaseLock) {
                if (isNotValid(databaseConnector)) {
                    databaseConnector = new DatabaseConnector(properties);
                }
            }
        }
        return databaseConnector;
    }

    /**
     * <code>SwordConnector</code>是否已关闭
     *
     * @return closed
     */
    public boolean isClosed() {
        return closed.get();
    }

    /**
     * 关闭<code>SwordConnector</code>
     */
    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            LOGGER.info("SwordConnector is closing...");
            tryClose(hBaseConnector);
            tryClose(httpConnector);
            tryClose(kafkaProducerConnector);
            tryClose(redisConnector);
            tryClose(databaseConnector);
            LOGGER.info("SwordConnector closed.");
        }
    }

    private static void tryClose(final AutoCloseable closeable) {
        try {
            if (closeable != null) {
                synchronized (closeable) {
                    closeable.close();
                }
            }
        } catch (Exception e) {
            LOGGER.warn("SwordConnectorException", e);
        }
    }
}
