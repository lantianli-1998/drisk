package com.roy.drisk.engine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc Engine配置类。由Spring从properties中加载以engine为前缀的相关配置
 */
@Component
@ConfigurationProperties(prefix = "engine")
public class EngineSettings {
    /**
     * 内部线程池核心线程数
     */
    private int executorCorePoolSize;
    /**
     * 内部线程池最大线程数
     */
    private int executorMaxPoolSize;
    /**
     * 内部线程池队列大小
     */
    private int executorQueueCapacity;
    /**
     * 是否开启统计信息
     */
    private boolean stats;
    /**
     * 规则引擎检查日志的Kafka Topic
     */
    private String journalTopic;
    /**
     * 规则引擎检查日志在HBase的存储表名
     */
    private String journalTableName;

    public int getExecutorCorePoolSize() {
        return executorCorePoolSize;
    }

    public void setExecutorCorePoolSize(int executorCorePoolSize) {
        this.executorCorePoolSize = executorCorePoolSize;
    }

    public int getExecutorMaxPoolSize() {
        return executorMaxPoolSize;
    }

    public void setExecutorMaxPoolSize(int executorMaxPoolSize) {
        this.executorMaxPoolSize = executorMaxPoolSize;
    }

    public int getExecutorQueueCapacity() {
        return executorQueueCapacity;
    }

    public void setExecutorQueueCapacity(int executorQueueCapacity) {
        this.executorQueueCapacity = executorQueueCapacity;
    }

    public boolean isStats() {
        return stats;
    }

    public void setStats(boolean stats) {
        this.stats = stats;
    }

    public String getJournalTopic() {
        return journalTopic;
    }

    public void setJournalTopic(String journalTopic) {
        this.journalTopic = journalTopic;
    }

    public String getJournalTableName() {
        return journalTableName;
    }

    public void setJournalTableName(String journalTableName) {
        this.journalTableName = journalTableName;
    }

    @Override
    public String toString() {
        return "EngineSettings{" +
                "executorCorePoolSize=" + executorCorePoolSize +
                ", executorMaxPoolSize=" + executorMaxPoolSize +
                ", executorQueueCapacity=" + executorQueueCapacity +
                ", stats=" + stats +
                ", journalTopic='" + journalTopic + '\'' +
                ", journalTableName='" + journalTableName + '\'' +
                '}';
    }
}
