package com.roy.drisk.connector.hbase;

import org.apache.hadoop.hbase.client.ResultScanner;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public interface ResultsExtractor<T> {
    T extractData(ResultScanner results) throws Exception;
}
