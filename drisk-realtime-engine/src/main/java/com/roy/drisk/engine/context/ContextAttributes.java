package com.roy.drisk.engine.context;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class ContextAttributes {
    private ConcurrentHashMap<String, Object> attibutes = new ConcurrentHashMap<>();

    public Object get(String key) {
        return attibutes.get(key);
    }

    public void put(String key, Object value) {
        attibutes.put(key, value);
    }

    public boolean containsKey(String key) {
        return attibutes.containsKey(key);
    }

    public Object remove(Object key) {
        return attibutes.remove(key);
    }
}
