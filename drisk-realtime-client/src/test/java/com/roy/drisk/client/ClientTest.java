package com.roy.drisk.client;

import com.roy.drisk.client.application.DriskClientBuilder;
import com.roy.drisk.client.application.DriskMQClient;
import com.roy.drisk.client.application.DriskNettyClient;
import com.roy.drisk.client.domain.message.client.KafkaMessage;
import com.roy.drisk.client.domain.message.client.MesageClient;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc
 */
public class ClientTest {

    public void createNettyClient(){
        DriskNettyClient client = DriskClientBuilder.newDriskNettyClient();
//        DriskClient client = DriskClientBuilder.newDriskNettyClient(true);
//        DriskClient client = DriskClientBuilder.newDriskNettyClient("otherClient.properties");
//        DriskClient client = DriskClientBuilder.newDriskNettyClient("otherClient.properties",true);
//        client.send()
    }

    public void createMQClient(){
        DriskMQClient client = new MesageClient();
        KafkaMessage message = new KafkaMessage("Json format message");
        client.sendMessage(message);
    }
}
