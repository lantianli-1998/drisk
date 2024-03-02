package com.roy.drisk.client.application;

import com.roy.drisk.client.domain.message.client.KafkaMessage;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc 通过kafka往非实时风控上报数据的客户端
 */
public interface DriskMQClient extends AutoCloseable {
    void sendMessage(KafkaMessage message);
}
