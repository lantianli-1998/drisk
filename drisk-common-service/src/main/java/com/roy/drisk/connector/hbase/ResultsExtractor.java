package com.roy.drisk.connector.hbase;

import org.apache.hadoop.hbase.client.ResultScanner;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public interface ResultsExtractor<T> {
    T extractData(ResultScanner results) throws Exception;
}
