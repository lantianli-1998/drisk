package com.roy.drisk.connector.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 默认的connectorconfiger的实现。默认加载类路径下的connector.properties
 */
public class DefaultConnectorConfiger implements ConnectorConfiger {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConnectorConfiger.class);
    private static final String DEFAULT_FILE = "connector-default.properties";
    private Properties properties = new Properties();

    public DefaultConnectorConfiger() {
        try {
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_FILE);
        if (is == null) {
            is = this.getClass().getResourceAsStream(DEFAULT_FILE);
        }
        properties.load(is);
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
