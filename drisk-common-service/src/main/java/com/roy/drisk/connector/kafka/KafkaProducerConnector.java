package com.roy.drisk.connector.kafka;

import com.roy.drisk.connector.config.ConnectorProperties;
import com.roy.drisk.connector.service.ClosedStatusAware;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class KafkaProducerConnector<K, V> implements AutoCloseable, ClosedStatusAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerConnector.class);
    private Properties properties;
    private KafkaMessageProducer<K, V> messageProducer;
    private AtomicBoolean closed = new AtomicBoolean(false);

    public KafkaProducerConnector(Properties properties) {
        this.properties = ConnectorProperties.filterProperties(properties, KafkaConstants.PROP_KEY);
        LOGGER.info("Create KafkaProducerConnector with {}", this.properties);
        this.messageProducer = new KafkaMessageProducer<>(new KafkaProducer<>(generateConfigs()));
    }

    private Map<String, Object> generateConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                properties.getProperty(KafkaConstants.BOOTSTRAP_SERVERS_CONFIG));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                properties.getProperty(KafkaConstants.KEY_SERIALIZER_CLASS_CONFIG));
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                properties.getProperty(KafkaConstants.VALUE_SERIALIZER_CLASS_CONFIG));
        props.put(ProducerConfig.ACKS_CONFIG,
                properties.getProperty(KafkaConstants.ACKS_CONFIG));
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,
                properties.getProperty(KafkaConstants.BUFFER_MEMORY_CONFIG));
        props.put(ProducerConfig.RETRIES_CONFIG,
                properties.getProperty(KafkaConstants.RETRIES_CONFIG));
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,
                properties.getProperty(KafkaConstants.BATCH_SIZE_CONFIG));
        props.put(ProducerConfig.CLIENT_ID_CONFIG,
                properties.getProperty(KafkaConstants.CLIENT_ID_CONFIG));
        props.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG,
                properties.getProperty(KafkaConstants.CONNECTIONS_MAX_IDLE_MS_CONFIG));
        props.put(ProducerConfig.LINGER_MS_CONFIG,
                properties.getProperty(KafkaConstants.LINGER_MS_CONFIG));
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,
                properties.getProperty(KafkaConstants.MAX_BLOCK_MS_CONFIG));
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,
                properties.getProperty(KafkaConstants.MAX_REQUEST_SIZE_CONFIG));
        props.put(ProducerConfig.RECEIVE_BUFFER_CONFIG,
                properties.getProperty(KafkaConstants.RECEIVE_BUFFER_CONFIG));
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
                properties.getProperty(KafkaConstants.REQUEST_TIMEOUT_MS_CONFIG));
        props.put(ProducerConfig.SEND_BUFFER_CONFIG,
                properties.getProperty(KafkaConstants.SEND_BUFFER_CONFIG));
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
                properties.getProperty(KafkaConstants.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION));
        props.put(ProducerConfig.METADATA_MAX_AGE_CONFIG,
                properties.getProperty(KafkaConstants.METADATA_MAX_AGE_CONFIG));
        props.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG,
                properties.getProperty(KafkaConstants.RECONNECT_BACKOFF_MS_CONFIG));
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,
                properties.getProperty(KafkaConstants.RETRY_BACKOFF_MS_CONFIG));
        return props;
    }

    public KafkaMessageProducer<K, V> getMessageProducer() {
        return messageProducer;
    }

    public boolean isClosed() {
        return closed.get();
    }

    public void close(long timeout, TimeUnit timeUnit) {
        if (closed.compareAndSet(false, true)) {
            messageProducer.close(timeout, timeUnit);
        }
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            LOGGER.info("KafkaProducerConnector is closing...");
            messageProducer.close();
            LOGGER.info("KafkaProducerConnector closed.");
        }
    }
}
