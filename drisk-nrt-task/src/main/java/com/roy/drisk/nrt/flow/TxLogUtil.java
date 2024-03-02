package com.roy.drisk.nrt.flow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roy.drisk.nrt.core.TxLog;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lantianli
 * @date 2021/11/8
 * @desc
 */
public class TxLogUtil {

    private static Map<String, Field> txLogMap;
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static TxLog convertFromMessage(String message){
        if(null == txLogMap){
            txLogMap = new HashMap<>();
            Arrays.stream(TxLog.class.getFields()).forEach(filed -> txLogMap.put(filed.getName(), filed));
        }

        TxLog txLog = new TxLog();
        try {
            final Map mapMessage = objectMapper.readValue(message, Map.class);
            mergeMap(mapMessage,txLog);
        } catch (IOException | IllegalAccessException e) {
            txLog.errorMessage = message;
        }
        return txLog;
    }

    private static void mergeMap(Map mapMessage, TxLog txLog) throws IllegalAccessException {
        for (Object key : mapMessage.keySet()) {
            final Object val = mapMessage.get(key);
            if(val instanceof Map){
                mergeMap((Map)val,txLog);
            }else{
                if(txLogMap.containsKey(key)){
                    txLogMap.get(key).set(txLog,val);
                }
            }
        }
    }
}
