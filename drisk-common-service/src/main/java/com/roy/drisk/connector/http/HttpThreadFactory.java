package com.roy.drisk.connector.http;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class HttpThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public HttpThreadFactory(String name) {
        namePrefix = name + "-httppool-"
                + poolNumber.getAndIncrement() + "-";
    }

    public Thread newThread(Runnable r) {
        return new Thread(r, namePrefix + threadNumber.getAndIncrement());
    }
}
