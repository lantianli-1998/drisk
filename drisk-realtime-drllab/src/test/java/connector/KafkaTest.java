package connector;

import com.roy.drisk.connector.kafka.KafkaMessageProducer;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class KafkaTest {

    @Before
    public void before(){
        System.setProperty("drisk.env","sit");
    }

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
