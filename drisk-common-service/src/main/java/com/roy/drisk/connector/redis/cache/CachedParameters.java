package com.roy.drisk.connector.redis.cache;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 以Redis为存储的参数服务
 */
public class CachedParameters {
    private static final int CACHE_SIZE = 500;
    private static final int CACHE_EXPIRE_SECONDS = 10;
    private static final RedisLocalCache cache = new RedisLocalCache(CACHE_SIZE, CACHE_EXPIRE_SECONDS);

    public static String getString(String key) {
        try {
            return cache.getString(key);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getString(String key, String defaultValue) {
        String res = getString(key);
        return res != null ? res : defaultValue;
    }

    public static int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return getInt(key);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    public static double getDouble(String key, double defaultValue) {
        try {
            return getDouble(key);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static List<String> getList(String key) {
        try {
            return cache.getList(key);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static Set<String> getSet(String key) {
        try {
            return cache.getSet(key);
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }

    public static Map<String, String> getHash(String key) {
        try {
            return cache.getHash(key);
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public static void invalidate(String key) {
        cache.invalidate(key);
    }

    private CachedParameters() {
    }
}
