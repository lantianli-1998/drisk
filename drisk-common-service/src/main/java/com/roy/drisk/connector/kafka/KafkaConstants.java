package com.roy.drisk.connector.kafka;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public final class KafkaConstants {
    public static final String PROP_KEY = "kafka";

    public static final String BOOTSTRAP_SERVERS_CONFIG = PROP_KEY + ".bootstrap.servers";
    public static final String KEY_SERIALIZER_CLASS_CONFIG = PROP_KEY + ".key.serializer";
    public static final String VALUE_SERIALIZER_CLASS_CONFIG = PROP_KEY + ".value.serializer";
    public static final String ACKS_CONFIG = PROP_KEY + ".acks";
    public static final String BUFFER_MEMORY_CONFIG = PROP_KEY + ".buffer.memory";
    public static final String RETRIES_CONFIG = PROP_KEY + ".retries";
    public static final String BATCH_SIZE_CONFIG = PROP_KEY + ".batch.size";
    public static final String CLIENT_ID_CONFIG = PROP_KEY + ".client.id";
    public static final String CONNECTIONS_MAX_IDLE_MS_CONFIG = PROP_KEY + ".connections.max.idle.ms";
    public static final String LINGER_MS_CONFIG = PROP_KEY + ".linger.ms";
    public static final String MAX_BLOCK_MS_CONFIG = PROP_KEY + ".max.block.ms";
    public static final String MAX_REQUEST_SIZE_CONFIG = PROP_KEY + ".max.request.size";
    public static final String RECEIVE_BUFFER_CONFIG = PROP_KEY + ".receive.buffer.bytes";
    public static final String REQUEST_TIMEOUT_MS_CONFIG = PROP_KEY + ".request.timeout.ms";
    public static final String SEND_BUFFER_CONFIG = PROP_KEY + ".send.buffer.bytes";
    public static final String MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION = PROP_KEY + ".max.in.flight.requests.per.connection";
    public static final String METADATA_MAX_AGE_CONFIG = PROP_KEY + ".metadata.max.age.ms";
    public static final String RECONNECT_BACKOFF_MS_CONFIG = PROP_KEY + ".reconnect.backoff.ms";
    public static final String RETRY_BACKOFF_MS_CONFIG = PROP_KEY + ".retry.backoff.ms";

    private KafkaConstants() {
    }
}
