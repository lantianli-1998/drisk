package com.roy.drisk.server.netty.config;

import com.roy.drisk.drools.DroolsEngine;
import com.roy.drisk.engine.service.Engine;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 * <code>EngineHandler</code>相关bean的配置类。
 */
@Configuration
public class EngineHandlerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineHandlerConfig.class);

    @Autowired
    private ApplicationContext applicationContext;
    private TransportSettings settings;

    @Autowired
    public void setSettings(TransportSettings settings) {
        this.settings = settings;
    }

    /**
     * 根据配置返回具体的Engine实现，
     * 暂时只支持DroolsEngine
     * @return Engine
     */
    @Bean
    public Engine rulesEngine() {
        String engine = settings.getSupportedEngine();
        Class engineClazz;
//       if ("drools".equalsIgnoreCase(engine)) {
//            engineClazz = DroolsEngine.class;
//        } else {
//            LOGGER.warn("Select engine not supported, choose drools for default.");
//            engineClazz = DroolsEngine.class;
//        }
        engineClazz = DroolsEngine.class;
        return (Engine) applicationContext.getBean(engineClazz);
    }

    /**
     * 根据配置构造EngineHandler使用的线程池，用于调用Engine运行业务方法，
     * 默认线程数为逻辑CPU数量的4倍
     *
     * @return EventExecutorGroup
     */
    @Bean
    public EventExecutorGroup engineHandlerExecutor() {
        int numThreads = settings.getHandlerThreads();
        if (numThreads <= 0) {
            numThreads = Runtime.getRuntime().availableProcessors() * 4;
        }
        return new DefaultEventExecutorGroup(numThreads, new DefaultThreadFactory("handlerExecutor"));
    }

    /**
     * 死锁线程探测器
     *
     * @return ThreadDeadlockDetector
     */
//    @Bean
//    public ThreadDeadlockDetector deadlockDetector() {
//        ThreadDeadlockDetector detector = new ThreadDeadlockDetector();
//        detector.addListener(new DefaultDeadlockListener());
//        return detector;
//    }
}
