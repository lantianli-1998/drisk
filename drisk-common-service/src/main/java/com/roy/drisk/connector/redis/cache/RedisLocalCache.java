package com.roy.drisk.connector.redis.cache;

import com.github.benmanes.caffeine.cache.RemovalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 为Redis增加前端缓存
 */
public class RedisLocalCache extends ObjectRedisLocalCache<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLocalCache.class);
    private static final CacheTransformer<Object> TRANSFORMER = (key, value) -> value;

    public RedisLocalCache(int maxSize, int expireSeconds) {
        super(maxSize, expireSeconds, TRANSFORMER);
    }

    public RedisLocalCache(int maxSize, int expireSeconds,
                           RemovalListener<String, Object> removalListener) {
        super(maxSize, expireSeconds, TRANSFORMER, removalListener);
    }

    public String getString(String key) {
        Object res = get(key);
        return res != null ? res.toString() : null;
    }

    @SuppressWarnings("unchecked")
    public List<String> getList(String key) {
        try {
            return (List<String>) get(key);
        } catch (ClassCastException e) {
            LOGGER.warn("Load cache " + key + " error", e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public Set<String> getSet(String key) {
        try {
            return (Set<String>) get(key);
        } catch (ClassCastException e) {
            LOGGER.warn("Load cache " + key + " error", e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getHash(String key) {
        try {
            return (Map<String, String>) get(key);
        } catch (ClassCastException e) {
            LOGGER.warn("Load cache " + key + " error", e);
            throw e;
        }
    }
}
