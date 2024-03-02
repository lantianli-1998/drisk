package com.roy.drisk.client.cli;


import com.roy.drisk.client.contract.ClientSettings;
import com.roy.drisk.client.domain.message.client.KafkaClient;

import java.io.IOException;
import java.util.Properties;

/**
 * @author lantianli
 * Date: 2016-07-15
 * Time: 09:23
 */
public class MessageRunner implements Runner {
    private RunParam param;
    private KafkaClient kafkaClient;

    public MessageRunner(RunParam param, Properties properties) throws IOException {
        this.param = param;
        ClientSettings settings = Settings.create(properties);
        kafkaClient = new KafkaClient(settings);
    }

    @Override
    public void run() {
        kafkaClient.send(param.getTopic(), param.getMessage());
    }

    @Override
    public void close() throws Exception {
        if (kafkaClient != null) {
            kafkaClient.close();
        }
    }
}
