package com.roy.drisk.connector.hbase;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class StringRowMapper implements RowMapper<Map<String, Map<String, String>>> {
    @Override
    public Map<String, Map<String, String>> mapRow(Result result, int rowNum) throws Exception {
        NavigableMap<byte[], NavigableMap<byte[], byte[]>> navigableMap = result.getNoVersionMap();
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        if (navigableMap != null && !navigableMap.isEmpty()) {
            navigableMap.forEach((familyBytes, childNavigableMap) -> {
                Map<String, String> childResultMap = new HashMap<>();
                childNavigableMap.forEach((qualifierBytes, valueBytes) ->
                        childResultMap.put(Bytes.toString(qualifierBytes), Bytes.toString(valueBytes)));
                resultMap.put(Bytes.toString(familyBytes), childResultMap);
            });
        }
        return resultMap;
    }
}
