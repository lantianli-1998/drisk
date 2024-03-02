package com.roy.drisk.commonservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 根据运行环境加载不同的properties文件。
 */
public class EnvProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnvProperties.class);
    private String schema;
    private String filePath;
    private Properties properties = new Properties();

    public EnvProperties(String file) {
        if (file == null || "".equals(file.trim())) {
            throw new IllegalArgumentException("Input file is empty.");
        }
        parseInput(file);
        loadProperties();
    }

    private void parseInput(String file) {
        int idx = file.indexOf(':');
        if (idx <= 0) {
            throw new IllegalArgumentException("Input format should be schema:file, got " + file);
        }
        if ((idx + 1) >= file.length()) {
            throw new IllegalArgumentException("Input format should be schema:file, got " + file);
        }
        this.schema = file.substring(0, idx);
        this.filePath = file.substring(idx + 1);
        checkScheme();
        checkFilePath();
    }

    private boolean isClassPath() {
        return "classpath".equals(this.schema);
    }

    private boolean isLocalFile() {
        return "file".equals(this.schema);
    }

    private void checkScheme() {
        if (!isClassPath() && !isLocalFile()) {
            throw new IllegalArgumentException("Scheme should be 'classpath' or 'file', got " + this.schema);
        }
    }

    private boolean isAbsolutePath() {
        return this.filePath.startsWith("/")
                || this.filePath.length() > 2 && this.filePath.charAt(1) == ':';
    }

    private void checkFilePath() {
        if (this.filePath == null || "".equals(this.filePath.trim())) {
            throw new IllegalArgumentException("Input file path is empty.");
        }
        if (isLocalFile() && !isAbsolutePath()) {
            this.filePath = new File(System.getProperty("user.dir"), this.filePath).getAbsolutePath();
        }
        if (isClassPath() && isAbsolutePath()) {
            this.filePath = this.filePath.substring(1);
        }
    }
   // filePath = connector.properties
    private List<String> buildFileList() {
        int idx = this.filePath.lastIndexOf('.');
        String prefix = this.filePath.substring(0, idx);
        String suffix = this.filePath.substring(idx);
        String env = EnvVariable.name();
        List<String> fileList = new ArrayList<>();
        fileList.add(this.filePath);
        fileList.add(prefix + "-" + env + suffix);
        return fileList;
    }

    private InputStream getInputStream(String fileName) throws FileNotFoundException {
        if (isClassPath()) {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                is = this.getClass().getClassLoader().getResourceAsStream(fileName);
            }
            return is;
        } else {
            return new FileInputStream(fileName);
        }
    }

    private void loadProperties() {
        List<String> fileNames = buildFileList();
        LOGGER.trace("EnvProperties fileList: {}", fileNames);
        for (String fileName : fileNames) {
            try (InputStream is = getInputStream(fileName)) {
                if (is != null) {
                    LOGGER.info("EnvProperties load: {}", fileName);
                    properties.load(is);
                }
            } catch (IOException ignored) {
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public Set<String> stringPropertyNames() {
        return properties.stringPropertyNames();
    }
}
