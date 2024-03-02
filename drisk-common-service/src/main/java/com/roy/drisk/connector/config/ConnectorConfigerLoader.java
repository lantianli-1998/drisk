package com.roy.drisk.connector.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 负责加载connector初始化需要的外部配置，
 * 加载机制如下：<br/>
 * 使用JDK提供的{@link ServiceLoader}机制查找是否有实现类，
 * 若有多个则只使用第一个，否则使用包自带的{@link DefaultConnectorConfiger}。
 */
public class ConnectorConfigerLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorConfigerLoader.class);
    private ConnectorConfiger connectorConfiger = null;

    public ConnectorConfigerLoader() {
        initConfiger();
    }

    private void initConfiger() {
        loadByServices();
        if (connectorConfiger != null) {
            LOGGER.info("Created ConnectorConfiger {} by service loader.", connectorConfiger);
            return;
        }
        loadDefault();
        if (connectorConfiger != null) {
            LOGGER.info("Created ConnectorConfiger {} by default.", connectorConfiger);
        }
    }

    private void loadByServices() {
        ServiceLoader<ConnectorConfiger> servicesLoader = ServiceLoader.load(ConnectorConfiger.class);
        boolean found = false;
        for (ConnectorConfiger configer : servicesLoader) {
            if (found) {
                LOGGER.info("Multiple ConnectorConfiger implementations found: {} ignored.", configer);
                continue;
            }
            connectorConfiger = configer;
            found = true;
        }
    }

    private void loadDefault() {
        connectorConfiger = new DefaultConnectorConfiger();
    }

    public ConnectorConfiger getConnectorConfiger() {
        return connectorConfiger;
    }
}
