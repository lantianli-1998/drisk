package com.roy.drisk.connector.redis;

import redis.clients.jedis.JedisCommands;

import java.io.Closeable;

/**
 * @author roy
 * @date 2021/10/27
 * @desc Redis客户端接口
 */
public interface CloseableJedisCommands extends JedisCommands, Closeable {
}
