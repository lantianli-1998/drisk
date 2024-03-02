package com.roy.drisk.connector.hbase;

import org.apache.hadoop.hbase.client.Result;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public interface RowMapper<T> {
    T mapRow(Result result, int rowNum) throws Exception;
}
