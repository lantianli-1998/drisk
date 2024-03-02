package com.roy.drisk.connector.config;

import java.util.Properties;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc connector初始化需要的外部配置将由实现了此接口的类加载。
 */
public interface ConnectorConfiger {
    Properties getProperties();
}
