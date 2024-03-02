package com.roy.drisk.connector.http;

import com.roy.drisk.connector.config.ConnectorProperties;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class PoolingHttpConnector implements HttpConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(PoolingHttpConnector.class);
    private Properties properties;
    private PoolingHttpClientConnectionManager manager;
    private HttpSyncClient syncClient;
    private HttpAsyncClient asyncClient;
    private PoolingNHttpClientConnectionManager nManager;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, r -> {
        Thread t = new Thread(r, "HttpIdleConnectionMonitor");
        t.setDaemon(true);
        return t;
    });
    private AtomicBoolean closed = new AtomicBoolean(false);

    public PoolingHttpConnector(Properties properties) {
        this.properties = ConnectorProperties.filterProperties(properties, HttpClientConstants.PROP_KEY);
        LOGGER.info("Create PoolingHttpConnector with {}", this.properties);
        init();
    }

    private void init() {
        this.manager = initManager();
        this.syncClient = new HttpSyncClient(HttpClients.custom()
                .setConnectionManager(manager)
                .build());
        try {
            this.nManager = initNManager();
            this.asyncClient = new HttpAsyncClient(HttpAsyncClients.custom()
                    .setConnectionManager(nManager)
                    .setThreadFactory(new HttpThreadFactory("async"))
                    .build());
            this.asyncClient.start();
        } catch (IOReactorException e) {
            throw new RuntimeException(e);
        }
        schedulerTask();
    }

    private PoolingHttpClientConnectionManager initManager() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(Integer.parseInt(properties.getProperty(HttpClientConstants.POOL_MAXTOTAL)));
        cm.setDefaultMaxPerRoute(Integer.parseInt(properties.getProperty(HttpClientConstants.POOL_DEFAULTMAXPERROUTE)));
        return cm;
    }

    private PoolingNHttpClientConnectionManager initNManager() throws IOReactorException {
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
        cm.setMaxTotal(Integer.parseInt(properties.getProperty(HttpClientConstants.POOL_MAXTOTAL)));
        cm.setDefaultMaxPerRoute(Integer.parseInt(properties.getProperty(HttpClientConstants.POOL_DEFAULTMAXPERROUTE)));
        return cm;
    }

    private void schedulerTask() {
        int idleTimeoutMillis = Integer.parseInt(properties.getProperty(HttpClientConstants.POOL_IDLETIMEOUTMILLIS));
        this.scheduler.scheduleAtFixedRate(new IdleConnectionMonitor(this.manager, this.nManager, idleTimeoutMillis),
                idleTimeoutMillis, idleTimeoutMillis, TimeUnit.MILLISECONDS);
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
            LOGGER.info("PoolingHttpConnector is closing...");
            this.scheduler.shutdown();
            try {
                this.syncClient.close();
            } catch (IOException e) {
                LOGGER.warn("PoolingHttpConnectorException", e);
            }
            try {
                this.asyncClient.close();
            } catch (IOException e) {
                LOGGER.warn("PoolingHttpConnectorException", e);
            }
            LOGGER.info("PoolingHttpConnector closed.");
        }
    }
}
