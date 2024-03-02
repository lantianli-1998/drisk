package com.roy.drisk.connector.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class KafkaMessageProducer<K, V> {
    private KafkaProducer<K, V> producer;

    public KafkaMessageProducer(KafkaProducer<K, V> producer) {
        this.producer = producer;
    }

    public Future<RecordMetadata> send(ProducerRecord<K, V> record) {
        return producer.send(record);
    }

    public List<PartitionInfo> partitionsFor(String topic) {
        return producer.partitionsFor(topic);
    }

    void close() {
        producer.close();
    }

    void close(long timeout, TimeUnit timeUnit) {
        producer.close(timeout, timeUnit);
    }

    public Future<RecordMetadata> send(String topic, K key, V value) {
        return producer.send(new ProducerRecord<>(topic, key, value));
    }

    public Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback) {
        return producer.send(record, callback);
    }

    public void flush() {
        producer.flush();
    }

    public Map<MetricName, ? extends Metric> metrics() {
        return producer.metrics();
    }
}
