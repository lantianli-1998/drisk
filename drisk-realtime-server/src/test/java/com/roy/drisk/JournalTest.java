package com.roy.drisk;

import com.roy.drisk.connector.kafka.KafkaMessageProducer;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author lantianli
 * @date 2021/11/14
 * @desc
 */
public class JournalTest {

    @Test
    public void testLG002() throws ExecutionException, InterruptedException {
        final KafkaMessageProducer<String, String> producer = DriskConnectorFactory.getKafkaMessageProducer();
        long checkTime = 1631635200000L;
        for(int i = 1 ; i < 10 ; i ++){
            checkTime = checkTime + (i*1000);
            String message = "{\"GWA\":{\"UENV\":{\"TERML_CITY_CD\":\"731\",\"OS\":\"0\",\"APP_NM\":\"hb\"," +
                    "\"TERML_ID\":\"493002407599521\",\"IP\":\"211.256.49.11\",\"TERML_PRV_CD\":\"18\"," +
                    "\"MBL_MAC\":\"00:01:6C:06:A6:11\",\"MBL_BRAND\":\"华为\"}},\"" +
                    "BSINF\":{\"TX_DT\":\"20211125\",\"MBL_NO\":\"13888888888\"," +
                    "\"JRN_NO\":\"20160612101122\",\"TX_CD\":\"2023094\",\"TX_AMT\":\"120.00\"," +
                    "\"MESSAGE_ID\":\"LOG20160612101123334455\",\"BUS_CNL\":\"DPAY\",\"USR_NO\":\"20929\"," +
                    "\"TX_TYP\":\"LG\",\"TX_TM\":\"111202\"},\"TABLE_NAME\":\"evaluateresults\",\"CHECK_TIME\":\""+checkTime+"\"" +
                    ",\"REQUEST_ID\":\"4442fa08-6a1a-41e6-9666-2036959c6f06\",\"RULE_TRIGGERED\":\",LG1001\"," +
                    "\"BUSI_CODE\":\"RFK2101\",\"TX_RST\":\"1\"}";
            final Future<RecordMetadata> sit_common_stream = producer.send("SIT_COMMON_STREAM", null, message);
            System.out.println(sit_common_stream.get());
        }
    }

    @Test
    public void journalTest() throws ExecutionException, InterruptedException {
        String message = "{\"GWA\":{\"UENV\":{\"TERML_CITY_CD\":\"731\",\"OS\":\"0\",\"APP_NM\":\"hb\"," +
                "\"TERML_ID\":\"493002407599521\",\"IP\":\"211.256.49.11\",\"TERML_PRV_CD\":\"18\"," +
                "\"MBL_MAC\":\"00:01:6C:06:A6:11\",\"MBL_BRAND\":\"华为\"}},\"" +
                "BSINF\":{\"TX_DT\":\"20211116\",\"MBL_NO\":\"13888888888\"," +
                "\"JRN_NO\":\"20160612101122\",\"TX_CD\":\"2023094\",\"TX_AMT\":\"120.00\"," +
                "\"MESSAGE_ID\":\"LOG20160612101123334455\",\"BUS_CNL\":\"DPAY\",\"USR_NO\":\"20929\"," +
                "\"TX_TYP\":\"LG\",\"TX_TM\":\"101104\"},\"TABLE_NAME\":\"evaluateresults\",\"CHECK_TIME\":\"1637030270264\"" +
                ",\"REQUEST_ID\":\"4442fa08-6a1a-41e6-9666-2036959c6f06\",\"RULE_TRIGGERED\":\",LG1001\"," +
                "\"BUSI_CODE\":\"RFK2101\",\"TX_RST\":\"1\"}";
        final KafkaMessageProducer<String, String> producer = DriskConnectorFactory.getKafkaMessageProducer();
        final Future<RecordMetadata> sit_common_stream = producer.send("SIT_COMMON_STREAM", null, message);
        System.out.println(sit_common_stream.get());
    }
}
