package com.roy.drisk.connector.redis;

import com.roy.drisk.connector.redis.batch.CloseableJedisBatchCommands;
import com.roy.drisk.connector.service.ClosedStatusAware;


/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public interface BaseRedisConnector extends AutoCloseable, ClosedStatusAware {
    CloseableJedisCommands getClient();
    CloseableJedisBatchCommands getBatchClient();
}
