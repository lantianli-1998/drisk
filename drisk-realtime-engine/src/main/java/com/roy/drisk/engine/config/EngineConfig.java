package com.roy.drisk.engine.config;

import com.roy.drisk.engine.handler.ContextEventHandler;
import com.roy.drisk.engine.handler.ContextMessageCodec;
import com.roy.drisk.engine.handler.DefaultContextMessageCodec;
import com.roy.drisk.engine.handler.EngineContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.lang.reflect.ParameterizedType;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc Engine相关的bean配置类
 */
@Configuration
public class EngineConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineConfig.class);
    @Autowired
    private ApplicationContext applicationContext;
    private EngineSettings settings;

    @Autowired
    public void setSettings(EngineSettings settings) {
        this.settings = settings;
    }

    /**
     * 默认的消息编解码器
     *
     * @return ContextMessageCodec
     */
    @Bean
    public ContextMessageCodec contextMessageCodec() {
        return new DefaultContextMessageCodec();
    }

    /**
     * EngineContextHandler列表，handler按其<code>@Order</code>注解的值升序排列
     *
     * @return EngineContextHandler列表
     */
    @Bean
    public List<EngineContextHandler> contextHandlers() {
        List<EngineContextHandler> handlers = applicationContext.getBeansOfType(EngineContextHandler.class)
                .values().stream().sorted(Comparator.comparing(handler ->
                        OrderUtils.getOrder(handler.getClass())))
                .collect(Collectors.toList());
        printHandlerChain(handlers);
        return handlers;
    }

    private void printHandlerChain(List<EngineContextHandler> handlers) {
        LOGGER.info("Loaded handler chain: ");
        for (int i = 0; i < handlers.size(); i++) {
            LOGGER.info("[{}] {}", i + 1, handlers.get(i));
        }
    }

    /**
     * ContextEventHandler与其关联的Event class的对应关系
     *
     * @return Event class与其对应的ContextEventHandler列表
     */
    @Bean
    public Map<Class, List<ContextEventHandler>> eventHandlers() {
        Map<Class, List<ContextEventHandler>> handlersMap =
                applicationContext.getBeansOfType(ContextEventHandler.class).values().stream()
                        .collect(Collectors.groupingBy(this::getGenericTypeClass));
        printEventHandlers(handlersMap);
        return handlersMap;
    }

    private void printEventHandlers(Map<Class, List<ContextEventHandler>> handlersMap) {
        LOGGER.info("Loaded event handlers: ");
        handlersMap.forEach((key, value) ->
                LOGGER.info("event[{}] by {}", key, value));
    }

    private Class getGenericTypeClass(ContextEventHandler handler) {
        ParameterizedType type = (ParameterizedType) handler.getClass().getGenericSuperclass();
        return (Class) type.getActualTypeArguments()[0];
    }

    /**
     * 定时任务线程池
     *
     * @return ThreadPoolTaskScheduler
     */
    @Bean
    @Lazy
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        scheduler.setDaemon(true);
        return scheduler;
    }

    /**
     * 根据配置构造的Engine内部任务线程池，
     * 若设置的核心线程数小于等于0，则默认为逻辑CPU个数的2倍，
     * 若设置的最大线程数为0，则默认为核心线程数的2倍，若为负数，则默认为<code>Integer.MAX_VALUE</code>，
     * 若设置的队列大小为0，则默认为核心线程数的200倍，若为负数，则默认为<code>Integer.MAX_VALUE</code>
     *
     * @return ThreadPoolTaskExecutor
     */
    @Bean
    public ThreadPoolTaskExecutor engineExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int corePoolSize = settings.getExecutorCorePoolSize();
        if (corePoolSize <= 0) {
            corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        }
        int maxPoolSize = settings.getExecutorMaxPoolSize();
        if (maxPoolSize == 0) {
            maxPoolSize = corePoolSize * 2;
        } else if (maxPoolSize < 0) {
            maxPoolSize = Integer.MAX_VALUE;
        }
        int queueCapacity = settings.getExecutorQueueCapacity();
        if (queueCapacity == 0) {
            queueCapacity = corePoolSize * 200;
        } else if (queueCapacity < 0) {
            queueCapacity = Integer.MAX_VALUE;
        }
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("engineExecutor-");
        return executor;
    }
}
