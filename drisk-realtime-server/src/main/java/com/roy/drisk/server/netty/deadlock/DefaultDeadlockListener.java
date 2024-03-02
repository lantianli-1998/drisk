package com.roy.drisk.server.netty.deadlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 * 线程死锁探测日志
 */
public class DefaultDeadlockListener implements ThreadDeadlockDetector.Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDeadlockListener.class);

    public void deadlockDetected(Thread[] threads) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("Deadlocked Threads:\n")
                .append("-------------------\n");
        for (Thread thread : threads) {
            sb.append(thread).append("\n");
            for (StackTraceElement ste : thread.getStackTrace()) {
                sb.append("\t").append(ste).append("\n");
            }
        }
        LOGGER.error(sb.toString());
    }
}
