package com.roy.drisk.nrt.repo;

import com.roy.drisk.commonservice.config.EnvProperties;

import java.util.Properties;

/**
 * @author roy
 * @date 2021/11/7
 * @desc 配置信息工厂
 */
public class ConfigRepo {

    private static Properties properties ;

    static {
        EnvProperties envProperties = new EnvProperties("classpath:connector.properties");
        EnvProperties taskProperties = new EnvProperties("classpath:task.properties");

        properties = new Properties();

        properties.putAll(envProperties.getProperties());
        properties.putAll(taskProperties.getProperties());
    }

    public static String getKafkaBootStrapServer(){
        return properties.getProperty("kafka.bootstrap.servers");
    }

    //TODO 获取实时风控检查后的Topic
    public static String getRtTopic(){
        return properties.getProperty("kafka.realtime.topic");
    }

    //TODO 获取FlowTask的kafka 消费者组
    public static String getFlowJobGroupId(){
        return properties.getProperty("kafka.flowjob.group");
    }

    public static String getFlowJobTopic() {return properties.getProperty("kafka.flowjob.topic");
    }

    public static String getStreamJobGroupId() {
        return properties.getProperty("kafka.streamjob.group");
    }
}
