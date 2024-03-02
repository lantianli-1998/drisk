package com.roy.drisk.nrt;

import com.roy.drisk.connector.redis.CloseableJedisCommands;
import com.roy.drisk.connector.service.DriskConnector;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import org.junit.Test;

/**
 * @author lantianli
 * @date 2021/11/14
 * @desc
 */
public class ConnectorTest {

    @Test
    public void redisTest(){
        final CloseableJedisCommands redisClient = DriskConnectorFactory.getRedisClient();
        System.out.println(redisClient);
    }
}
