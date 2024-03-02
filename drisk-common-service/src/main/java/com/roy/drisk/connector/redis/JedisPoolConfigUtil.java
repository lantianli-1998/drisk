package com.roy.drisk.connector.redis;

import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc JedisPoolConfig初始化工具类
 */
public class JedisPoolConfigUtil {
    public static JedisPoolConfig newPoolConfig(Properties properties) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(properties.getProperty(RedisConstants.POOL_MAXTOTAL)));
        config.setMaxIdle(Integer.parseInt(properties.getProperty(RedisConstants.POOL_MAXIDLE)));
        config.setMinIdle(Integer.parseInt(properties.getProperty(RedisConstants.POOL_MINIDLE)));
        config.setMaxWaitMillis(Long.parseLong(properties.getProperty(RedisConstants.POOL_MAXWAITMILLIS)));
        return config;
    }
}
