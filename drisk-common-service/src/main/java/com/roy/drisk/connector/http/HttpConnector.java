package com.roy.drisk.connector.http;

import com.roy.drisk.connector.service.ClosedStatusAware;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public interface HttpConnector extends AutoCloseable, ClosedStatusAware {
    HttpSyncClient getSyncClient();

    HttpAsyncClient getAsyncClient();
}
