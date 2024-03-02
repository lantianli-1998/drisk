package com.roy.drisk.client.domain.message.client;

import com.roy.drisk.client.contract.ClientSettings;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc
 */
public class KafkaClient implements Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaClient.class);
    public static final String KEY_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String VALUE_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer";
    private ClientSettings settings;
    private Producer<String, String> producer;

    public KafkaClient(final ClientSettings settings) {
        this.settings = settings;
        this.producer = createProducer();
    }

    private Producer<String, String> createProducer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.getKafkaBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER_CLASS);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER_CLASS);
        props.put(ProducerConfig.ACKS_CONFIG, settings.getKafkaAcks());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, settings.getKafkaBufferMemory());
        props.put(ProducerConfig.RETRIES_CONFIG, settings.getKafkaRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, settings.getKafkaBatchSize());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, settings.getKafkaClientId());
        props.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, settings.getKafkaConnectionsMaxIdleMs());
        props.put(ProducerConfig.LINGER_MS_CONFIG, settings.getKafkaLingerMs());
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, settings.getKafkaMaxBlockMs());
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, settings.getKafkaMaxRequestSize());
        props.put(ProducerConfig.RECEIVE_BUFFER_CONFIG, settings.getKafkaReceiveBufferBytes());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, settings.getKafkaRequestTimeoutMs());
        props.put(ProducerConfig.SEND_BUFFER_CONFIG, settings.getKafkaSendBufferBytes());
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, settings.getKafkaMaxInFlightRequestsPerConnection());
        props.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, settings.getKafkaMetadataMaxAgeMs());
        props.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, settings.getKafkaReconnectBackoffMs());
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, settings.getKafkaRetryBackoffMs());
        return new KafkaProducer<>(props);
    }

    @Override
    public void close() {
        this.producer.close();
    }

    public void send(final String topic, final String key, final String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        LOGGER.debug("Kafka send: {}", record);
        producer.send(record);
    }

    public void send(final String topic, final String value) {
        send(topic, null, value);
    }

    public void flush() {
        producer.flush();
    }
}
