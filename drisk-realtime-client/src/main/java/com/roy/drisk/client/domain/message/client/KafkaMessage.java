package com.roy.drisk.client.domain.message.client;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc
 */
public class KafkaMessage {

    private String topic;
    private String key;
    private String message;

    public KafkaMessage() {
    }

    public KafkaMessage(String message) {
        this.message = message;
    }

    public KafkaMessage(String topic, String key, String message) {
        this.topic = topic;
        this.key = key;
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
