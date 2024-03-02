package com.roy.drisk.nrt.core.flow.services;

import com.roy.drisk.connector.hbase.HBaseUtil;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import com.roy.drisk.nrt.core.TxLog;
import org.apache.htrace.fasterxml.jackson.core.JsonProcessingException;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author roy
 * @date 2021/11/8
 * @desc
 */
public class TxLogService{

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void saveTxlog2HBase(TxLog txLog) throws JsonProcessingException {
        final HBaseUtil hBaseUtil = new HBaseUtil(DriskConnectorFactory.getHBaseConnection());
        hBaseUtil.put("TXLOG_ERROR", txLog.REQUEST_ID, "info", "record", objectMapper.writeValueAsBytes(txLog));
    }
}
