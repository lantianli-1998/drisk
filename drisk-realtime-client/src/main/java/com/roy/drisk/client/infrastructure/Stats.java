package com.roy.drisk.client.infrastructure;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author roy
 * @date 2021/10/26
 * @desc 用于记录统计信息的工具类
 */
public class Stats {
    private final ConcurrentHashMap<String, StatInfo> stats = new ConcurrentHashMap<>();

    public void update(String key, long duration) {
        StatInfo stat = stats.get(key);
        if (stat == null) {
            stat = new StatInfo();
        }
        stat.update(duration);
        stats.putIfAbsent(key, stat);
    }

    public void clear() {
        stats.clear();
    }

    public Map<String, StatInfo> getStats() {
        return Collections.unmodifiableMap(stats);
    }

    public static final class StatInfo {
        private AtomicLong min = new AtomicLong(Long.MAX_VALUE);
        private AtomicLong max = new AtomicLong(0);
        private AtomicLong total = new AtomicLong(0);
        private AtomicLong counts = new AtomicLong(0);

        public void update(long d) {
            if (d < min.get()) {
                min.set(d);
            }
            if (d > max.get()) {
                max.set(d);
            }
            total.addAndGet(d);
            counts.incrementAndGet();
        }

        public long getMin() {
            return min.get();
        }

        public long getMax() {
            return max.get();
        }

        public long getTotal() {
            return total.get();
        }

        public long getCounts() {
            return counts.get();
        }

        public long getAverage() {
            long c = getCounts();
            if (c == 0)
                return 0;
            return getTotal() / c;
        }
    }
}
