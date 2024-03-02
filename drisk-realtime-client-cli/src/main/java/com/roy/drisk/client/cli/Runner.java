package com.roy.drisk.client.cli;

/**
 * User: QC
 * Date: 2016-07-15
 * Time: 09:23
 */
public interface Runner extends AutoCloseable {
    void run() throws Throwable;
}
