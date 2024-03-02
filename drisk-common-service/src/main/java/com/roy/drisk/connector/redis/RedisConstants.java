package com.roy.drisk.connector.redis;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc Redis连接器常量类
 */
public final class RedisConstants {
    public static final String PROP_KEY = "redis";

    public static final String MODE = PROP_KEY + ".mode";
    public static final String HOST_PORT = PROP_KEY + ".hostAndPort";
    public static final String MAX_REDIRECTS = PROP_KEY + ".clusterMaxRedirects";
    public static final String PASSWORD = PROP_KEY + ".password";
    public static final String DATABASE = PROP_KEY + ".database";
    public static final String TIMEOUT = PROP_KEY + ".timeout";
    public static final String POOL_MAXTOTAL = PROP_KEY + ".poolMaxTotal";
    public static final String POOL_MAXIDLE = PROP_KEY + ".poolMaxIdle";
    public static final String POOL_MINIDLE = PROP_KEY + ".poolMinIdle";
    public static final String POOL_MAXWAITMILLIS = PROP_KEY + ".poolMaxWaitMillis";
    public static final String SLOWLOGMILLIS = PROP_KEY + ".slowlogMillis";

    private RedisConstants() {
    }
}
