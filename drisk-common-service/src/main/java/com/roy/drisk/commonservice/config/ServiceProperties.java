package com.roy.drisk.commonservice.config;

import java.util.Set;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 服务统一配置
 */
public class ServiceProperties {
    private static final String DEFAULT_FILE = "classpath:com/roy/drisk/service/service.properties";
    private static final EnvProperties envProperties = new EnvProperties(DEFAULT_FILE);

    public static String getProperty(String key) {
        return envProperties.getProperties().getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return envProperties.getProperties().getProperty(key, defaultValue);
    }

    public static Set<String> stringPropertyNames() {
        return envProperties.getProperties().stringPropertyNames();
    }
}
