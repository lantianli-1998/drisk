package com.roy.drisk.connector.hbase;

import org.apache.hadoop.hbase.client.Table;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public interface TableCallback<T> {
    T doInTable(Table table) throws Exception;
}
