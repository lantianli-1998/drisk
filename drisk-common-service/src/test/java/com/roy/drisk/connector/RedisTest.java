package com.roy.drisk.connector;

import com.roy.drisk.connector.redis.batch.CloseableJedisBatchCommands;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class RedisTest {
//    @Test
//    public void testExist() {
//        boolean ret = SwordConnectorFactory.getRedisClient().exists("test-key");
//        Assert.assertEquals(false, ret);
//    }

    @Test
    public void testBatch(){
        CloseableJedisBatchCommands jedisBatch = DriskConnectorFactory.getRedisBatch();

        long s = System.currentTimeMillis();
        // batch write
        for (int i = 0; i < 10000; i++) {
            jedisBatch.set("k" + i, "v1" + i);
        }
        jedisBatch.sync();

        // batch read
        for (int i = 0; i < 10000; i++) {
            jedisBatch.get("k" + i);
        }
        List<Object> batchResult = jedisBatch.syncAndReturnAll();

        long t = System.currentTimeMillis() - s;
        System.out.println("batch cost: " + t + "ms.");

        System.out.println(batchResult.size());
    }

//    @Test
//    public void testClient(){
//    	CloseableJedisCommands jedisCommands = SwordConnectorFactory.getRedisClient();
//
//    	long s = System.currentTimeMillis();
//    	// batch write
//    	for (int i = 0; i < 10000; i++) {
//    		jedisCommands.set("k" + i, "v1" + i);
//        }
//
//    	// batch read
//        for (int i = 0; i < 10000; i++) {
//        	jedisCommands.get("k" + i);
//        }
//        long t = System.currentTimeMillis() - s;
//        System.out.println("client cost: " + t + "ms.");
//
//    }

    @After
    public void after() throws IOException {
        DriskConnectorFactory.close();
    }
}
