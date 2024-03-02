package com.roy.drisk.connector.hbase;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class StringResultsExtractor implements ResultsExtractor<Map<String, Map<String, Map<String, String>>>> {
    private final StringRowMapper rowMapper = new StringRowMapper();

    public Map<String, Map<String, Map<String, String>>> extractData(ResultScanner results) throws Exception {
        Map<String, Map<String, Map<String, String>>> rs = new LinkedHashMap<>();
        int rowNum = 0;
        for (Result result : results) {
            rs.put(Bytes.toString(result.getRow()), this.rowMapper.mapRow(result, rowNum++));
        }
        return rs;
    }
}