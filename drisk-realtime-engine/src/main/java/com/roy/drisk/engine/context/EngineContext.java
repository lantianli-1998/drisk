package com.roy.drisk.engine.context;

import com.roy.drisk.context.EngineContextIter;
import com.roy.drisk.engine.service.EngineService;
import com.roy.drisk.engine.handler.ContextEventHandler;
import com.roy.drisk.message.ContextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author roy
 * @date 2021/10/27
 * @desc Engine上下文。每次请求均由Engine产生一个新的Context。
 * 用于向业务提供上下文环境的消息、服务、内部事件等基础设施。
 */
public class EngineContext implements EngineContextIter {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineContext.class);
    private ContextMessage contextMessage;
    private EngineService engineService;
    private Map<Class, List<ContextEventHandler>> eventHandlers;
    private final ContextAttributes attributes = new ContextAttributes();

    EngineContext() {
    }

    void setContextMessage(ContextMessage contextMessage) {
        this.contextMessage = contextMessage;
    }

    void setEngineService(EngineService engineService) {
        this.engineService = engineService;
    }

    void setEventHandlers(Map<Class, List<ContextEventHandler>> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    public ContextMessage message() {
        return contextMessage;
    }

    public EngineService service() {
        return engineService;
    }

    public ContextAttributes attr() {
        return attributes;
    }

    public Set<Class> getEventTypes() {
        return Collections.unmodifiableSet(eventHandlers.keySet());
    }

    public <E> void sendEvent(E event) {
        List<ContextEventHandler> handlers = eventHandlers.get(event.getClass());
        if (handlers == null) {
            LOGGER.warn("Event handler for type {} not found.", event.getClass());
            return;
        }
        handlers.forEach(handler -> handler.handleEvent(this, event));
    }
}
