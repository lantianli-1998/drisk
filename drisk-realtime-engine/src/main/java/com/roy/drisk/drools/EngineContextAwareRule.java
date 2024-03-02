package com.roy.drisk.drools;

import com.roy.drisk.engine.context.EngineContext;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 * 规则服务是否需要感知{@link EngineContext}，实现此接口的规则服务，
 * 由Engine在初始化后调用{@link #setEngineContext(EngineContext)}
 * 设置本次请求的{@link EngineContext}。
 */
public interface EngineContextAwareRule {
    /**
     * 设置EngineContext
     *
     * @param engineContext EngineContext
     */
    void setEngineContext(EngineContext engineContext);
}
