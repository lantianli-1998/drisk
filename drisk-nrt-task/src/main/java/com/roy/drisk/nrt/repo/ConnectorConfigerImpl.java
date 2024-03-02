package com.roy.drisk.nrt.repo;

import com.roy.drisk.commonservice.config.EnvProperties;
import com.roy.drisk.connector.config.ConnectorConfiger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc <code>SwordConnector</code>配置类，按运行环境加载配置目录下的connector属性文件
 */
public class ConnectorConfigerImpl implements ConnectorConfiger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorConfigerImpl.class);
    private static final String FILE_NAME = "/connector.properties";
    private Properties properties;

    public ConnectorConfigerImpl() {
        loadConnectorProperties();
    }

    private void loadConnectorProperties() {
//        String fileName = FileUtil.getFilePath(EngineConstants.CONFIG_DIR, PROP_NAME);
//        EnvProperties envProperties = new EnvProperties("file:" + fileName);
//        this.properties = envProperties.getProperties();
//        LOGGER.info("Load Connector properties: {}", properties);

        EnvProperties baseProperties = new EnvProperties("classpath:/connector.properties");
        this.properties = baseProperties.getProperties();

        EnvProperties envProperties = new EnvProperties("classpath:/connector-sit.properties");
        if(this.properties != null){
            this.properties.putAll(envProperties.getProperties());
        }else{
            this.properties = envProperties.getProperties();
        }
        LOGGER.info("Load Connector properties: {}", properties);
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
