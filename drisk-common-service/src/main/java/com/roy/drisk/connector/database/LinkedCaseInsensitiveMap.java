package com.roy.drisk.connector.database;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class LinkedCaseInsensitiveMap<V> extends LinkedHashMap<String, V> {
    private final Map<String, String> caseInsensitiveKeys;
    private final Locale locale;

    public LinkedCaseInsensitiveMap() {
        this(null);
    }

    public LinkedCaseInsensitiveMap(Locale locale) {
        super();
        this.caseInsensitiveKeys = new HashMap<>();
        this.locale = (locale != null ? locale : Locale.getDefault());
    }

    public LinkedCaseInsensitiveMap(int initialCapacity) {
        this(initialCapacity, null);
    }

    public LinkedCaseInsensitiveMap(int initialCapacity, Locale locale) {
        super(initialCapacity);
        this.caseInsensitiveKeys = new HashMap<>(initialCapacity);
        this.locale = (locale != null ? locale : Locale.getDefault());
    }

    @Override
    public V put(String key, V value) {
        String oldKey = this.caseInsensitiveKeys.put(convertKey(key), key);
        if (oldKey != null && !oldKey.equals(key)) {
            super.remove(oldKey);
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> map) {
        if (map.isEmpty()) {
            return;
        }
        for (Map.Entry<? extends String, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean containsKey(Object key) {
        return (key instanceof String && this.caseInsensitiveKeys.containsKey(convertKey((String) key)));
    }

    @Override
    public V get(Object key) {
        if (key instanceof String) {
            return super.get(this.caseInsensitiveKeys.get(convertKey((String) key)));
        } else {
            return null;
        }
    }

    @Override
    public V remove(Object key) {
        if (key instanceof String) {
            return super.remove(this.caseInsensitiveKeys.remove(convertKey((String) key)));
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        this.caseInsensitiveKeys.clear();
        super.clear();
    }

    protected String convertKey(String key) {
        return key.toLowerCase(this.locale);
    }
}
