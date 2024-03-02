package com.roy.drisk.connector.hbase;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
class RowMapperResultsExtractor<T> implements ResultsExtractor<List<T>> {
    private final RowMapper<T> rowMapper;

    public RowMapperResultsExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public List<T> extractData(ResultScanner results) throws Exception {
        List<T> rs = new ArrayList<>();
        int rowNum = 0;
        for (Result result : results) {
            rs.add(this.rowMapper.mapRow(result, rowNum++));
        }
        return rs;
    }
}
