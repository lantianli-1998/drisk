package com.roy.drisk.connector.http;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public final class HttpClientConstants {
    public static final String PROP_KEY = "httpclient";

    public static final String POOL_MAXTOTAL = PROP_KEY + ".pool.maxTotal";
    public static final String POOL_DEFAULTMAXPERROUTE = PROP_KEY + ".pool.defaultMaxPerRoute";
    public static final String POOL_IDLETIMEOUTMILLIS = PROP_KEY + ".pool.idleTimeoutMillis";

    private HttpClientConstants() {
    }
}
