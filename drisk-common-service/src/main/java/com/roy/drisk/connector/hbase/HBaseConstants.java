package com.roy.drisk.connector.hbase;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public final class HBaseConstants {
    public static final String PROP_KEY = "hbase";

    public static final String ZOOKEEPER_QUORUM = PROP_KEY + ".zookeeper.quorum";
    public static final String ZOOKEEPER_CLIENT_PORT = PROP_KEY + ".zookeeper.property.clientPort";
    public static final String HBASE_CLIENT_SCANNER_TIMEOUT_PERIOD = PROP_KEY + ".client.scanner.timeout.period";
    public static final String HBASE_CONNECTION_THREADS_MAX = PROP_KEY + ".hconnection.threads.max";
    public static final String HBASE_CONNECTION_THREADS_CORE = PROP_KEY + ".hconnection.threads.core";
    public static final String HBASE_RPC_TIMEOUT_KEY = PROP_KEY + ".rpc.timeout";

    private HBaseConstants() {
    }
}
