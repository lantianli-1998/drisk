package com.roy.drisk.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roy.drisk.services.GeneralService;
import com.roy.drisk.services.GeneralServiceFactory;
import com.roy.drisk.connector.hbase.HBaseConnection;
import com.roy.drisk.connector.http.HttpAsyncClient;
import com.roy.drisk.connector.http.HttpSyncClient;
import com.roy.drisk.connector.kafka.KafkaMessageProducer;
import com.roy.drisk.connector.redis.CloseableJedisCommands;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import com.roy.drisk.exception.DatabaseInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author roy
 * @date 2021/10/27
 * @desc Engine所能提供的公共service。
 */
@Component
public class EngineService {
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    private void initEngineService() {
        try {
            Class.forName("com.roy.drisk.services.GeneralServiceFactory");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    private void destory() {
        DriskConnectorFactory.close();
    }

    /**
     * 取Json ObjectMapper
     *
     * @return ObjectMapper
     */
    public ObjectMapper json() {
        return objectMapper;
    }

    /**
     * 取HBase客户端
     *
     * @return HBaseConnection
     */
    public HBaseConnection hbase() {
        return DriskConnectorFactory.getHBaseConnection();
    }

    /**
     * 取同步访问的Http客户端
     *
     * @return HttpSyncClient
     */
    public HttpSyncClient http() {
        return DriskConnectorFactory.getHttpClient();
    }

    /**
     * 取异步访问的Http客户端
     *
     * @return HttpAsyncClient
     */
    public HttpAsyncClient asyncHttp() {
        return DriskConnectorFactory.getHttpAsyncClient();
    }

    /**
     * 取Kafka生产者客户端
     *
     * @return KafkaMessageProducer
     */
    public KafkaMessageProducer<String, String> kafka() {
        return DriskConnectorFactory.getKafkaMessageProducer();
    }

    /**
     * 取Redis客户端
     *
     * @return CloseableJedisCommands
     */
    public CloseableJedisCommands redis() {
        return DriskConnectorFactory.getRedisClient();
    }

    /**
     * 取数据库连接
     *
     * @return Connection
     */
    public Connection db() {
        try {
            return DriskConnectorFactory.getDatabaseConnection();
        } catch (SQLException e) {
            throw new DatabaseInvalidException(e);
        }
    }

    /**
     * 按数据源名称取数据库连接
     *
     * @param dsName 数据源名称
     * @return Connection
     */
    public Connection db(String dsName) {
        try {
            return DriskConnectorFactory.getDatabaseConnection();
        } catch (SQLException e) {
            throw new DatabaseInvalidException(e);
        }
    }

    /**
     * 取业务GeneralService服务
     *
     * @param clazz 实现GeneralService的泛型类
     * @param <T>   实现GeneralService的泛型类
     * @return T
     */
    public <T extends GeneralService> T getService(Class<T> clazz) {
        return GeneralServiceFactory.getService(clazz);
    }
}
