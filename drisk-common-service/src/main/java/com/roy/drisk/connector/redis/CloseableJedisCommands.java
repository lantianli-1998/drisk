package com.roy.drisk.connector.redis;

import redis.clients.jedis.JedisCommands;

import java.io.Closeable;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc Redis客户端接口
 */
public interface CloseableJedisCommands extends JedisCommands, Closeable {
}
