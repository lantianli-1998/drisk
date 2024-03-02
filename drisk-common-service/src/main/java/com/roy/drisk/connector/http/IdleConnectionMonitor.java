package com.roy.drisk.connector.http;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.nio.conn.NHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class IdleConnectionMonitor implements Runnable {
    private HttpClientConnectionManager manager;
    private NHttpClientConnectionManager nManager;
    private int idleTimeoutMillis;

    public IdleConnectionMonitor(HttpClientConnectionManager manager, int idleTimeoutMillis) {
        this.manager = manager;
        this.idleTimeoutMillis = idleTimeoutMillis;
    }

    public IdleConnectionMonitor(NHttpClientConnectionManager nManager, int idleTimeoutMillis) {
        this.nManager = nManager;
        this.idleTimeoutMillis = idleTimeoutMillis;
    }

    public IdleConnectionMonitor(HttpClientConnectionManager manager,
                                 NHttpClientConnectionManager nManager, int idleTimeoutMillis) {
        this.manager = manager;
        this.nManager = nManager;
        this.idleTimeoutMillis = idleTimeoutMillis;
    }

    @Override
    public void run() {
        if (manager != null) {
            manager.closeExpiredConnections();
            manager.closeIdleConnections(idleTimeoutMillis, TimeUnit.MILLISECONDS);
        }
        if (nManager != null) {
            nManager.closeExpiredConnections();
            nManager.closeIdleConnections(idleTimeoutMillis, TimeUnit.MILLISECONDS);
        }
    }
}
