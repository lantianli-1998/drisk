package com.roy.drisk.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author roy
 * @date 2021/10/26
 * @desc 消息格式
 */
public enum MessageFormat {
    JSON(0), KRYO(1);

    private static Map<Integer, MessageFormat> findTable = new HashMap<>();

    static {
        findTable.put(JSON.getValue(), JSON);
        findTable.put(KRYO.getValue(), KRYO);
    }

    private int value;

    MessageFormat(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MessageFormat byValue(int n) {
        return findTable.get(n);
    }
}
