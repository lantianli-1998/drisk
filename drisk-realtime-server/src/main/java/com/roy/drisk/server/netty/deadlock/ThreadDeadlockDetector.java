package com.roy.drisk.server.netty.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 * 线程死锁探测
 */
public class ThreadDeadlockDetector {
    private final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
    private final Timer threadCheck = new Timer("ThreadDeadlockDetector", true);
    private final Collection<Listener> listeners = new CopyOnWriteArraySet<>();
    private static final int DEFAULT_DEADLOCK_CHECK_PERIOD = 10000;

    public ThreadDeadlockDetector() {
        this(DEFAULT_DEADLOCK_CHECK_PERIOD);
    }

    public ThreadDeadlockDetector(int deadlockCheckPeriod) {
        threadCheck.schedule(new TimerTask() {
            public void run() {
                checkForDeadlocks();
            }
        }, 10, deadlockCheckPeriod);
    }

    private void checkForDeadlocks() {
        long[] ids = findDeadlockedThreads();
        if (ids != null && ids.length > 0) {
            Thread[] threads = new Thread[ids.length];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = findMatchingThread(mbean.getThreadInfo(ids[i]));
            }
            fireDeadlockDetected(threads);
        }
    }

    private long[] findDeadlockedThreads() {
        if (mbean.isSynchronizerUsageSupported())
            return mbean.findDeadlockedThreads();
        else
            return mbean.findMonitorDeadlockedThreads();
    }

    private void fireDeadlockDetected(Thread[] threads) {
        for (Listener l : listeners) {
            l.deadlockDetected(threads);
        }
    }

    private Thread findMatchingThread(ThreadInfo inf) {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getId() == inf.getThreadId()) {
                return thread;
            }
        }
        throw new IllegalStateException("Deadlocked Thread not found");
    }

    public boolean addListener(Listener l) {
        return listeners.add(l);
    }

    public boolean removeListener(Listener l) {
        return listeners.remove(l);
    }

    public interface Listener {
        void deadlockDetected(Thread[] deadlockedThreads);
    }
}