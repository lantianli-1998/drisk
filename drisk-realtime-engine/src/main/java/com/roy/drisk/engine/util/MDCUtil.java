package com.roy.drisk.engine.util;

import org.slf4j.MDC;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class MDCUtil {
    /**
     * 默认的日志MDC Key
     */
    public static final String LOG_MDC_KEY = "requestId";
    /**
     * 默认的日志MDC Value
     */
    public static final String LOG_MDC_DEFAULT = "UNKNOWN";

    /**
     * 设置MDC属性
     *
     * @param key   MDC Key
     * @param value MDC Value
     */
    public static void putMDCKey(String key, String value) {
        MDC.put(key, value != null ? value : LOG_MDC_DEFAULT);
    }

    /**
     * 设置MDC默认Key属性
     *
     * @param value MDC Value
     */
    public static void putMDCKey(String value) {
        putMDCKey(LOG_MDC_KEY, value);
    }

    /**
     * 删除MDC Key
     *
     * @param key MDC Key
     */
    public static void removeMDCKey(String key) {
        MDC.remove(key);
    }

    /**
     * 删除MDC默认的Key
     */
    public static void removeMDCKey() {
        removeMDCKey(LOG_MDC_KEY);
    }
}
