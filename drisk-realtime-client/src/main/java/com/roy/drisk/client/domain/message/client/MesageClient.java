package com.roy.drisk.client.domain.message.client;

import com.roy.drisk.client.application.DriskMQClient;
import com.roy.drisk.client.contract.ClientSettings;
import com.roy.drisk.client.infrastructure.SettingsRepo;
import com.roy.drisk.exception.DriskClientException;
import com.sun.scenario.Settings;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author roy
 * @date 2021/10/26
 * @desc
 */
public class MesageClient implements DriskMQClient {
    private ClientSettings settings;
    private KafkaClient kafkaClient;

    public MesageClient() throws DriskClientException{
        try {
            settings = SettingsRepo.create();
            kafkaClient = new KafkaClient(settings);
        } catch (IOException e) {
            throw new DriskClientException("创建MQ客户端失败");
        }
    }

    public MesageClient(Properties properties) throws DriskClientException {
        ClientSettings settings;
        try{
            if(null == properties){
                settings = SettingsRepo.create();
            }else {
                settings = SettingsRepo.create(properties);
            }
            kafkaClient = new KafkaClient(settings);
        }catch (IOException e){
            throw new DriskClientException("创建MQ客户端失败");
        }

    }

    @Override
    public void close() throws Exception {
        if (kafkaClient != null) {
            kafkaClient.close();
        }
    }

    @Override
    public void sendMessage(KafkaMessage message) {
        if(StringUtils.isEmpty(message.getTopic())){
            message.setTopic(settings.getKafkaDefaultTopic()) ;
        }
        if(StringUtils.isEmpty(message.getTopic())){
            kafkaClient.send(message.getTopic(),message.getMessage());
        }else{
            kafkaClient.send(message.getTopic(),message.getKey(),message.getMessage());
        }
    }
}
