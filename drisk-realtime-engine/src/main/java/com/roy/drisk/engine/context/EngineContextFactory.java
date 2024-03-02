package com.roy.drisk.engine.context;

import com.roy.drisk.engine.service.EngineService;
import com.roy.drisk.engine.handler.ContextEventHandler;
import com.roy.drisk.message.ContextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc <code>EngineContext</code>工厂类，初始化新的<code>EngineContext</code>
 */
@Component
public class EngineContextFactory {
    @Autowired
    private EngineService engineService;
    @Resource
    private Map<Class, List<ContextEventHandler>> eventHandlers;

    public EngineContext newContext(ContextMessage message) {
        EngineContext context = new EngineContext();
        context.setEngineService(engineService);
        context.setContextMessage(message);
        context.setEventHandlers(eventHandlers);
        return context;
    }
}
