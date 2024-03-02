package com.roy.drisk.connector.http;

import com.roy.drisk.connector.config.ConnectorProperties;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class SimpleHttpConnector implements HttpConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpConnector.class);
    private Properties properties;
    private HttpSyncClient syncClient;
    private HttpAsyncClient asyncClient;
    private AtomicBoolean closed = new AtomicBoolean(false);

    public SimpleHttpConnector(Properties properties) {
        this.properties = ConnectorProperties.filterProperties(properties, HttpClientConstants.PROP_KEY);
        LOGGER.info("Create SimpleHttpConnector with {}", this.properties);
        init();
    }

    private void init() {
        this.syncClient = new HttpSyncClient(HttpClients.createDefault());
        this.asyncClient = new HttpAsyncClient(HttpAsyncClients.custom()
                .setThreadFactory(new HttpThreadFactory("async"))
                .build());
        this.asyncClient.start();
    }

    @Override
    public HttpSyncClient getSyncClient() {
        return this.syncClient;
    }

    @Override
    public HttpAsyncClient getAsyncClient() {
        return this.asyncClient;
    }

    @Override
    public boolean isClosed() {
        return closed.get();
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            LOGGER.info("SimpleHttpConnector is closing...");
            try {
                this.syncClient.close();
            } catch (IOException e) {
                LOGGER.warn("SimpleHttpConnectorException", e);
            }
            try {
                this.asyncClient.close();
            } catch (IOException e) {
                LOGGER.warn("SimpleHttpConnectorException", e);
            }
            LOGGER.info("SimpleHttpConnector closed.");
        }
    }
}
