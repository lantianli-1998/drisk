package com.roy.drisk.client.infrastructure;

import com.roy.drisk.client.contract.ClientSettings;
import com.roy.drisk.exception.DriskClientException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc
 */
public class SettingsRepo {
    private static final String DEFAULT_SETTINGS = "drisk-client.properties";

    public static ClientSettings create() throws IOException {
        InputStream inputStream = SettingsRepo.class.getResourceAsStream(DEFAULT_SETTINGS);
        if (inputStream == null)
            throw new DriskClientException(DEFAULT_SETTINGS);
        ClientSettings settings = new ClientSettings();
        transfer(loadProperties(inputStream), settings);
        return settings;
    }

    public static ClientSettings create(Properties properties) throws IOException {
        ClientSettings settings = create();
        transfer(properties, settings);
        return settings;
    }

    private static Properties loadProperties(InputStream file) throws IOException {
        Properties properties = new Properties();
        properties.load(file);
        return properties;
    }

    private static void transfer(Properties properties, ClientSettings settings) {
        try {
            for (String key : properties.stringPropertyNames()) {
                Method method = settings.getClass().getMethod(toSetMethodName(key), String.class);
                method.invoke(settings, properties.getProperty(key));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        }
    }

    private static String toSetMethodName(String key) {
        StringBuilder sb = new StringBuilder("set");
        boolean nextUpperCase = true;
        for (char c : key.toCharArray()) {
            if (c == '.') {
                nextUpperCase = true;
            } else if (nextUpperCase) {
                sb.append(Character.toUpperCase(c));
                nextUpperCase = false;
            } else {
                sb.append(c);
                nextUpperCase = false;
            }
        }
        return sb.toString();
    }
}
