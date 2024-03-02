package com.roy.drisk.connector.redis.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.roy.drisk.connector.redis.CloseableJedisCommands;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 为Redis增加前端缓存，变换为对象存储。
 */
public class ObjectRedisLocalCache<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectRedisLocalCache.class);
    private int maxSize;
    private int expireSeconds;
    private LoadingCache<String, T> cache;
    private RemovalListener<String, T> removalListener;
    private CacheTransformer<T> transformer;

    public ObjectRedisLocalCache(int maxSize, int expireSeconds, CacheTransformer<T> transformer) {
        this(maxSize, expireSeconds, transformer, null);
    }

    public ObjectRedisLocalCache(int maxSize, int expireSeconds, CacheTransformer<T> transformer,
                                 RemovalListener<String, T> removalListener) {
        this.maxSize = maxSize;
        this.expireSeconds = expireSeconds;
        this.transformer = transformer;
        this.removalListener = removalListener;
        if (this.removalListener == null) {
            this.removalListener = (key, value, cause) -> LOGGER.debug("Remove cache for {}: {}", key, value);
        }
        initCache();
    }

    private void initCache() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(this.maxSize)
                .expireAfterWrite(this.expireSeconds, TimeUnit.SECONDS)
                .removalListener(this.removalListener)
                .build(key -> {
                    try (CloseableJedisCommands redisClient =
                                 DriskConnectorFactory.getRedisClient()) {
                        String type = redisClient.type(key);
                        Object value;
                        if ("string".equalsIgnoreCase(type)) {
                            value = redisClient.get(key);
                        } else if ("list".equalsIgnoreCase(type)) {
                            value = redisClient.lrange(key, 0, -1);
                        } else if ("set".equalsIgnoreCase(type)) {
                            value = redisClient.smembers(key);
                        } else if ("zset".equalsIgnoreCase(type)) {
                            value = redisClient.zrange(key, 0, -1);
                        } else if ("hash".equalsIgnoreCase(type)) {
                            value = redisClient.hgetAll(key);
                        } else {
                            value = null;
                        }
                        LOGGER.debug("Load cache for {}: {}", key, value);
                        return transformer.transform(key, value);
                    }
                });
    }

    public T get(String key) {
        try {
            return cache.get(key);
        } catch (Exception e) {
            LOGGER.warn("Load cache " + key + " error", e);
            throw new RuntimeException(e);
        }
    }

    public void refresh(String key) {
        cache.refresh(key);
    }

    public void invalidate(String key) {
        cache.invalidate(key);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public interface CacheTransformer<T> {
        T transform(String key, Object value);
    }
}
