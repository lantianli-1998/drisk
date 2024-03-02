package com.roy.drisk.connector;

import com.roy.drisk.connector.kafka.KafkaMessageProducer;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import org.junit.After;
import org.junit.Test;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class KafkaTest {
    @Test
    public void testSender() {
        KafkaMessageProducer<String, String> producer = DriskConnectorFactory.getKafkaMessageProducer();
        producer.send("test-topic", null, "TEST");
        producer.flush();
    }

    @After
    public void after() {
        DriskConnectorFactory.close();
    }
}
