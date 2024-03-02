package com.roy.drisk.connector.http;

import com.roy.drisk.connector.service.ClosedStatusAware;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public interface HttpConnector extends AutoCloseable, ClosedStatusAware {
    HttpSyncClient getSyncClient();

    HttpAsyncClient getAsyncClient();
}
