package com.roy.drisk.connector.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 负责加载包默认的connector-default.properties文件。
 */
public class ConnectorProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorProperties.class);
    private static final String DEFAULT_FILE = "connector-default.properties";
    private static final Properties defaultProperties = new Properties();

    static {
        try {
            loadProperties();
        } catch (IOException e) {
            LOGGER.error("ConnectorPropertiesException", e);
        }
    }

    private static void loadProperties() throws IOException {
        try (InputStream is = ConnectorProperties.class.getResourceAsStream(DEFAULT_FILE)) {
            if (is != null) {
                defaultProperties.load(is);
            }
        }
    }

    /**
     * 取默认<code>Properties</code>
     *
     * @return Properties
     */
    public static Properties newProperties() {
        Properties p = new Properties();
        p.putAll(defaultProperties);
        return p;
    }

    /**
     * 取默认<code>Properties</code>，并使用<code>userProperties</code>覆盖
     *
     * @return Properties
     */
    public static Properties newProperties(Properties userProperties) {
        Properties p = newProperties();
        if (userProperties != null) {
            p.putAll(userProperties);
        }
        return p;
    }

    /**
     * 使用<code>prefix</code>作为前缀过滤输入的<code>properties</code>
     *
     * @return Properties
     */
    public static Properties filterProperties(Properties properties, String prefix) {
        Properties p = new Properties();
        if (properties == null) {
            return p;
        }
        properties.stringPropertyNames().stream()
                .filter(key -> key.startsWith(prefix))
                .forEach(key -> p.setProperty(key, properties.getProperty(key)));
        return p;
    }
}
