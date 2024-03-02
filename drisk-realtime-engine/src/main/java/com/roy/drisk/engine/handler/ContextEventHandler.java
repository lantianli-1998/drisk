package com.roy.drisk.engine.handler;

import com.roy.drisk.engine.context.EngineContext;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 实现了<code>ContextEventHandler</code>接口的类表明自己需要处理特定类型的事件。
 */
public interface ContextEventHandler<E> {
    /**
     * 处理事件
     *
     * @param context EngineContext
     * @param event   event
     */
    void handleEvent(final EngineContext context, E event);
}
