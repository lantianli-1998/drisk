package com.roy.drisk.engine.handler;

import com.roy.drisk.engine.context.EngineContext;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 * <code>EngineContextHandler</code>由Engine加载，加载顺序由其上标注的
 * <code>org.springframework.core.annotation.Order</code>注解标明，
 * 其中before方法按Order从小到大的顺序执行，after方法按Order从大到小的顺序执行。
 * 即：Order数值越大越接近engine的实际process流程。
 */
public interface EngineContextHandler {
    /**
     * 消息前处理
     *
     * @param context EngineContext
     */
    void before(EngineContext context);

    /**
     * 消息后处理
     *
     * @param context EngineContext
     */
    void after(EngineContext context);
}
