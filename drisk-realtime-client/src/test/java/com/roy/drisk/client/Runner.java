package com.roy.drisk.client;

/**
 * @author lantianli
 * Date: 2016-07-15
 * Time: 09:23
 */
public interface Runner extends AutoCloseable {
    void run() throws Throwable;
}
